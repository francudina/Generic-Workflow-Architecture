package com.generic.workflow.library.activities;

import com.generic.workflow.library.AdvancedExecutable;
import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.workflows.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class Activity<S extends ExecutableStatus> extends AdvancedExecutable<S> {

    protected String activityId;
    protected S activityStatus;

    private Workflow<S> parentWorkflow;

    private Condition<S> defaultCondition;

    private List<ActivityLink<S, Activity<S>>> nextActivityOptions;


    public Activity() {
        this.activityId = UUID.randomUUID().toString();
        this.nextActivityOptions = new LinkedList<>();
    }


    private void addNextLink(ActivityLink<S, Activity<S>> nextActivityLink) {
        this.nextActivityOptions.add(nextActivityLink);
    }

    @Override
    public final S status() {
        return this.activityStatus;
    }


    public final Activity<S> setOrResetDefaultCondition(Condition<S> defaultCondition) {
        this.defaultCondition = defaultCondition;
        return this;
    }

    /**
     * Setting parent {@link Workflow} for getting additional metadata about {@link Workflow} execution (e.g. starting {@link Activity}).
     *
     * @param workflow {@link Workflow} of current {@link Activity} instance
     */
    public final void setParentWorkflow(Workflow<S> workflow) {
        this.parentWorkflow = workflow;
    }

    /**
     * Used when there is only one {@link Activity} possible after current (this) {@link Activity}.
     * Method uses default {@link Condition} set by {@link #setOrResetDefaultCondition(Condition)} method.
     *
     * @param executeActivity execute next {@link Activity}
     * @return next {@link Activity} to execute
     */
    public final Activity<S> next(Activity<S> executeActivity) {

        if (!this.hasDefaultCondition()) {
            log.warn("Activity ignored because default activity condition isn't set!");
            return this;
        }

        return this.next(this.defaultCondition, executeActivity);
    }

    /**
     * Used when there is only one {@link Activity} possible after current (this) {@link Activity}.
     * Method uses given {@link Condition} set as param.
     *
     * @param onCondition {@link Condition} which is used to determine next {@link Activity}
     * @param executeActivity {@link Activity} to execute next based on {@link Condition}
     * @return next {@link Activity} to execute
     */
    public final Activity<S> next(Condition<S> onCondition, Activity<S> executeActivity) {

        executeActivity.setParentWorkflow(this.parentWorkflow);

        // adding link and default condition
        this.addNextLink(new ActivityLink<>(onCondition, executeActivity));

        return executeActivity;
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
    public final Activity<S> when(Condition<S> onCondition, Activity<S> executeActivity) {

        executeActivity.setParentWorkflow(this.parentWorkflow);
        this.addNextLink(new ActivityLink<>(onCondition, executeActivity));

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
    public final Activity<S> when(Condition<S> onCondition, Activity<S> executeActivity, Activity<S> elseExecuteActivity) {

        if (this.isLastPossibleActivityAdded() || this.nextActivityOptions.size() > 0) {
            log.warn("It's not possible to add two or more if-else 'when' clauses in the same workflow! " +
                    "Create another workflow branch or use 'when' clause with onCondition & executeActivity params! " +
                    "Ignoring and returning current activity!");
            return this;
        }

        // adding if activity on condition
        this.when(onCondition, executeActivity);
        // adding else activity on negated condition
        this.when(onCondition.negate(), elseExecuteActivity);

        // only one if-else case is possible per Activity
        this.setLastPossibleActivity();

        return this;
    }

    /**
     * Builds {@link Workflow} from multiple given {@link Activity} objects.
     *
     * @return {@link Workflow} of multiple {@link Activity} objects
     */
    public final Workflow<S> build() {
        return this.parentWorkflow.build();
    }

    /**
     * Return root/starting {@link Activity} object.
     *
     * @return root/starting {@link Activity} object
     */
    public final Activity<S> root() {
        return this.parentWorkflow.root();
    }

    private void setLastPossibleActivity() {
        this.parentWorkflow.setLastPossibleActivity();
    }

    private boolean isLastPossibleActivityAdded() {
        return this.parentWorkflow.isLastPossibleActivityAdded();
    }

    private boolean hasDefaultCondition() {
        return this.defaultCondition != null;
    }
}
