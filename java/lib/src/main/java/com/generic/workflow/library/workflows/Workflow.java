package com.generic.workflow.library.workflows;

import com.generic.workflow.library.AdvancedExecutable;
import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.payload.ExecutionPayload;

import javax.naming.OperationNotSupportedException;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class Workflow
        <S extends ExecutableStatus, P extends ExecutionPayload<?>>
        extends AdvancedExecutable<S, P>
{

    private final Logger log = Logger.getLogger(this.getClass().getName());

    protected String workflowId;
    protected S workflowStatus;

    protected boolean lastActivityOptionAdded;
    protected boolean startingActivityReturned;

    protected Activity<S, P> startingActivity;
    protected Condition<S> defaultCondition;


    public Workflow() {
        this.workflowId = UUID.randomUUID().toString();
        this.lastActivityOptionAdded = false;
        this.startingActivityReturned = false;
    }

    /**
     * Get {@link Workflow} status.
     *
     * @return {@link Workflow} status
     */
    @Override
    public final S status() {
        return this.workflowStatus;
    }

    /**
     * Adding first {@link Activity} to the whole {@link Workflow}. This is mostly done to make it easier
     * to track the {@link Workflow} starting point, and to override others {@link Workflow} variables.
     *
     * @param activity {@link Activity} which is used to start the {@link Workflow} iteration, it can also
     *                                 be another root node of another {@link Workflow} object to append
     *                                 it to new {@link Workflow} iteration
     * @return last usable {@link Activity} to continue adding another nodes
     * @throws OperationNotSupportedException @see {@link Activity#build()}
     */
    public final Activity<S, P> startWith(Activity<S, P> activity) throws OperationNotSupportedException {

        Activity<S, P> firstActivity = activity;
        // checking if given activity has already another workflow!
        if (activity.hasWorkflow()) {

            // getting root of the given activity to set starting activity!
            firstActivity = activity.root();
            // getting workflow instance to get params!
            var workflow = firstActivity.build();

            // important for part with multiple "when" methods are called!
            this.lastActivityOptionAdded = workflow.lastActivityOptionAdded;
        }

        // setting parent workflow
        firstActivity.setParentWorkflow(this);
        this.startingActivity = firstActivity;

        // getting last element of the workflow to continue adding new elements, if possible!
        return activity.lastUsableChild();
    }

    /**
     * Builds {@link Workflow} from multiple given {@link Activity} objects.
     *
     * @return {@link Workflow} of multiple {@link Activity} objects
     */
    public final Workflow<S, P> build() {
        return this;
    }

    /**
     * Executes {@link Workflow} from {@link #startingActivity}.
     *
     * @param payloadInput input data for execution
     * @return true if execution succeeded, false otherwise
     */
    @Override
    public final boolean execute(P payloadInput) {
        log.info(String.format("Executing workflow with id: %s", this.workflowId));
        boolean passed = new WorkflowExecutionHandler<>(this).execute(payloadInput);
        log.info(String.format("Finished executing workflow with id: %s - passed? %s", this.workflowId, passed));
        return passed;
    }

    /**
     * Return root/starting {@link Activity} object.
     *
     * @return root/starting {@link Activity} object
     */
    public Activity<S, P> root() {
        this.startingActivityReturned = true;
        return this.startingActivity;
    }

    /**
     * Mark that last {@link Activity} was added, and there's no chance to add another {@link Activity}
     * to current {@link Workflow} object.
     */
    public final void setLastPossibleActivity() {
        this.lastActivityOptionAdded = true;
    }

    /**
     * Check's if last option was added to the {@link Workflow} {@link Activity}.
     *
     * @return true, if last option was added to the {@link Workflow} {@link Activity}, false otherwise
     */
    public final boolean isLastActivityOptionAdded() {
        return this.lastActivityOptionAdded;
    }

    /**
     * Check's if {@link Workflow#startingActivity} was returned
     * when {@link Workflow#root()} method was triggered.
     *
     * @return if {@link Workflow#startingActivity} was returned
     *          when {@link Workflow#root()} method was triggered, false otherwise
     */
    public final boolean isStartingActivityReturned() {
        return this.startingActivityReturned;
    }

    public Activity<S, P> getStartingActivity() {
        return startingActivity;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public S getWorkflowStatus() {
        return workflowStatus;
    }

    public Condition<S> getDefaultCondition() {
        return defaultCondition;
    }

    public void setDefaultCondition(Condition<S> defaultCondition) {
        this.defaultCondition = defaultCondition;
    }
}
