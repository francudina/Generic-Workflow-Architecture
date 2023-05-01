package com.generic.workflow.library.activities;

import com.generic.workflow.library.AdvancedExecutable;
import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.payload.ExecutionPayload;
import com.generic.workflow.library.utils.ExecutionUtils;
import com.generic.workflow.library.workflows.Workflow;

import javax.naming.OperationNotSupportedException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public abstract class Activity extends AdvancedExecutable {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    protected String activityId;
    protected ExecutableStatus activityStatus;
    protected ExecutionPayload<?> executionResult;

    private Workflow parentWorkflow;

    private final List<ActivityLink> nextActivityOptions;

    private boolean isIfElseCase;


    public Activity() {
        this.activityId = ExecutionUtils.newIdForClass(this);
        this.nextActivityOptions = new LinkedList<>();
        this.isIfElseCase = false;
    }


    @Override
    public final ExecutableStatus status() {
        return this.activityStatus;
    }

    /**
     * It is not possible to suspend {@link Activity} during execution.
     *
     * @return always false (not suspended)
     */
    @Override
    public final boolean suspend() {
        return false;
    }

    public final Activity setOrResetDefaultCondition(Condition<ExecutableStatus> defaultCondition)
            throws OperationNotSupportedException
    {
        if (!this.hasWorkflow())
            throw new OperationNotSupportedException(
                    "Cannot add default condition to Activity if there's no workflow set!");

        this.parentWorkflow.setDefaultCondition(defaultCondition);
        return this;
    }

    /**
     * Setting parent {@link Workflow} for getting additional metadata
     * about {@link Workflow} execution (e.g. starting {@link Activity}).
     * {@link Workflow} is set recursively for each child {@link Activity}.
     *
     * @param workflow {@link Workflow} of current {@link Activity} instance
     */
    public final void setParentWorkflow(Workflow workflow) {
        // setting parent workflow
        this.parentWorkflow = workflow;
        // iterating over children activities and setting new workflow instance!
        for (ActivityLink link : this.nextActivityOptions) {
            link.getActivity().setParentWorkflow(workflow);
        }
    }

    /**
     * Used when there is only one {@link Activity} possible after current (this) {@link Activity}.
     * Method uses default {@link Condition} set by {@link #setOrResetDefaultCondition(Condition)} method.
     *
     * @param executeActivity execute next {@link Activity}
     * @return next {@link Activity} to execute
     */
    public final Activity next(Activity executeActivity) throws OperationNotSupportedException {

        if (!this.hasDefaultCondition()) {
            log.warning("Activity ignored because default activity condition isn't set!");
            return this;
        }

        return this.next(this.parentWorkflow.getDefaultCondition(), executeActivity);
    }

    /**
     * Used when there is only one {@link Activity} possible after current (this) {@link Activity}.
     * Method uses given {@link Condition} set as param.
     *
     * @param onCondition {@link Condition} which is used to determine next {@link Activity}
     * @param executeActivity {@link Activity} to execute next based on {@link Condition}
     * @return next {@link Activity} to execute
     */
    public final Activity next(
            Condition<ExecutableStatus> onCondition,
            Activity executeActivity
    ) throws OperationNotSupportedException {

        if (this.isStartingActivityReturned())
            throw new OperationNotSupportedException("Starting activity was returned, so there's no way " +
                    "to add another activity to existing workflow activity!");

        // if there was already if-else before added!
        if (this.isLastActivityOptionAdded())
            throw new OperationNotSupportedException("It's not possible to add if 'when' clause in the same workflow after if-else 'when' clause! " +
                    "Create another workflow branch! Ignoring and returning current activity!");

        // check if there's already added last option in got activity!
        boolean hasLast = executeActivity.isLastActivityOptionAdded();
        if (hasLast)
            // add it to current workflow!
            this.setLastPossibleActivity();

        // update children activities for current workflow
        executeActivity.setParentWorkflow(this.parentWorkflow);

        // adding link and default condition
        this.addNextLink(new ActivityLink(onCondition, executeActivity));

        // return last child of that activity, so to continue with piping!
        return executeActivity.lastUsableChild();
    }

    /**
     * Executing {@link Activity} if {@link Condition} if fulfilled.
     * <p>
     * When there's multiple Conditions with same test() outcome, "First Come, First Served"
     * logic will be used while picking next activity.
     *
     * @param onCondition {@link Condition} which should be fulfilled
     * @param executeActivity {@link Activity} to execute on fulfilled {@link Condition}
     * @return current (this) {@link Activity}
     */
    public final Activity when(
            Condition<ExecutableStatus> onCondition,
            Activity executeActivity
    ) throws OperationNotSupportedException {

        if (this.isStartingActivityReturned())
            throw new OperationNotSupportedException("Starting activity was returned, so there's no way " +
                    "to add another activity to existing workflow activity!");

        // if there was already if-else before added!
        if (this.isLastActivityOptionAdded() && !this.isIfElseCase)
            throw new OperationNotSupportedException("It's not possible to add if 'when' clause in the same workflow after if-else 'when' clause! " +
                    "Create another workflow branch! Ignoring and returning current activity!");

        // check if there's already added last option in got activity!
        boolean hasLast = executeActivity.isLastActivityOptionAdded();
        if (hasLast)
            // add it to current workflow!
            this.setLastPossibleActivity();

        // update children activities for current workflow
        executeActivity.setParentWorkflow(this.parentWorkflow);

        this.addNextLink(new ActivityLink(onCondition, executeActivity));

        return this;
    }

    /**
     * Executing {@link Activity} if {@link Condition} if fulfilled, otherwise else {@link Activity}.
     * <p>
     * This method could be called only once, after that new workflow should be created
     * or instead of {@link #when(Condition, Activity, Activity)} (this) method, {@link #when(Condition, Activity)}
     * method should be used for more than 2 conditions linked to the same parent {@link Activity}.
     *
     * @param onCondition {@link Condition} which should be fulfilled
     * @param executeActivity {@link Activity} to execute on fulfilled {@link Condition}
     * @param elseExecuteActivity else {@link Activity} that needs to be executed
     * @return current (this) {@link Activity}
     */
    public final Activity when(
            Condition<ExecutableStatus> onCondition,
            Activity executeActivity,
            Activity elseExecuteActivity
    ) throws OperationNotSupportedException {

        if (this.isStartingActivityReturned()) {
            log.warning("Starting activity was returned, so there's no way " +
                    "to add another activity to existing workflow activity!");
            return this;
        }

        if (this.isLastActivityOptionAdded() || this.hasChildren()) {
            log.warning("It's not possible to add two or more if-else 'when' clauses in the same workflow! " +
                    "Create another workflow branch or use 'when' clause with onCondition & executeActivity params! " +
                    "Ignoring and returning current activity!");
            return this;
        }

        // special case when adding multiple nested left and right subtrees into root node!
        this.isIfElseCase = true;

        // adding if activity on condition
        this.when(onCondition, executeActivity);
        // adding else activity on negated condition
        this.when(onCondition.negate(), elseExecuteActivity);

        // only one if-else case is possible per Activity
        this.setLastPossibleActivity();

        // reset if-else case to false
        this.isIfElseCase = false;

        return this;
    }

    /**
     * Builds {@link Workflow} from multiple given {@link Activity} objects.
     *
     * @return {@link Workflow} of multiple {@link Activity} objects
     */
    public final Workflow build() throws OperationNotSupportedException {
        if (!this.hasWorkflow())
            throw new OperationNotSupportedException("Cannot build Activity if there's no workflow set!");
        return this.parentWorkflow.build();
    }

    /**
     * Return root/starting {@link Activity} object.
     *
     * @return root/starting {@link Activity} object
     */
    public final Activity root() throws OperationNotSupportedException {
        if (!this.hasWorkflow())
            throw new OperationNotSupportedException("Cannot get root Activity if there's no workflow set!");
        return this.parentWorkflow.root();
    }

    /**
     * Method executes {@link #lastUsableChild()} method recursively while trying to find
     * {@link Activity} with multiple children {@link Activity} objects or no objects at all.
     * <p>
     * This is useful when there's need to get to the last {@link Workflow} {@link Activity}
     * object starting from root {@link Activity} node.
     *
     * @return last {@link Activity} in the branch
     */
    public final Activity lastUsableChild() {
        // checks if current activity has multiple or no children at all
        if (this.hasMoreThanOneChildren() || !this.hasChildren())
            return this;

        // case if activity contains only one child
        return this.nextActivityOptions.get(0)
                .getActivity()
                .lastUsableChild();
    }

    public final ExecutionPayload<?> getExecutionResult() {
        return this.executionResult;
    }

    /**
     * Method check's if {@link Activity} contains more than one children {@link Activity} objects.
     *
     * @return true if {@link Activity} contains more than one children {@link Activity} objects, false otherwise
     */
    public final boolean hasMoreThanOneChildren() {
        return this.nextActivityOptions.size() > 1;
    }

    /**
     * Method check's if {@link Activity} contains children {@link Activity} objects.
     *
     * @return true if {@link Activity} contains children {@link Activity} objects, false otherwise
     */
    public final boolean hasChildren() {
        return this.nextActivityOptions.size() > 0;
    }

    public final boolean hasWorkflow() {
        return this.parentWorkflow != null;
    }

    public List<ActivityLink> getNextActivityOptions() {
        return nextActivityOptions;
    }

    public String getActivityId() {
        return activityId;
    }

    private void setLastPossibleActivity() {
        this.parentWorkflow.setLastPossibleActivity();
    }

    private boolean isLastActivityOptionAdded() {
        return this.parentWorkflow != null && this.parentWorkflow.isLastActivityOptionAdded();
    }

    private boolean isStartingActivityReturned() {
        return this.parentWorkflow != null && this.parentWorkflow.isStartingActivityReturned();
    }

    private void addNextLink(ActivityLink nextActivityLink) {
        this.nextActivityOptions.add(nextActivityLink);
    }

    private boolean hasDefaultCondition() {
        return this.parentWorkflow != null && this.parentWorkflow.getDefaultCondition() != null;
    }
}
