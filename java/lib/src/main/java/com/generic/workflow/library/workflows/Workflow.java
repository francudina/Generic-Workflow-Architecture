package com.generic.workflow.library.workflows;

import com.generic.workflow.library.AdvancedExecutable;
import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.payload.ExecutionPayload;
import com.generic.workflow.library.utils.ExecutionUtils;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class Workflow extends AdvancedExecutable {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    protected String workflowId;
    protected ExecutableStatus workflowStatus;

    protected boolean lastActivityOptionAdded;
    protected boolean startingActivityReturned;

    protected Activity startingActivity;
    protected Condition<ExecutableStatus> defaultCondition;

    protected List<Activity> activityHistory;


    public Workflow() {
        this.workflowId = ExecutionUtils.newIdForClass(this);
        this.lastActivityOptionAdded = false;
        this.startingActivityReturned = false;
        this.isSuspended = false;
        this.activityHistory = new ArrayList<>();
    }

    /**
     * Get {@link Workflow} status.
     *
     * @return {@link Workflow} status
     */
    @Override
    public final ExecutableStatus status() {
        return this.workflowStatus;
    }

    /**
     * Testing if condition satisfies result status!
     *
     * @param inputToTest condition to test
     * @return if condition is satisfied
     */
    @Override
    public boolean testAfter(Condition<ExecutableStatus> inputToTest) {
        return inputToTest.testAfter(this.status());
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
    public final Activity startWith(Activity activity) throws OperationNotSupportedException {

        Activity firstActivity = activity;
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
    public final Workflow build() {
        return this;
    }

    /**
     * Executes {@link Workflow} from {@link #startingActivity}.
     *
     * @param payloadInput input data for execution
     * @return true if execution succeeded, false otherwise
     */
    @Override
    public final boolean execute(ExecutionPayload<?> payloadInput) throws Exception {
        log.info(String.format("Executing workflow with id: %s", this.workflowId));

        this.workflowStatus = ExecutableStatus.IN_PROGRESS;
        boolean passed = new WorkflowExecutionHandler(this)
                .execute(payloadInput);
        this.workflowStatus = passed? ExecutableStatus.FINISHED : ExecutableStatus.FAILED;

        log.info(String.format("Finished executing workflow with id: %s - passed? %s", this.workflowId, passed));
        return passed;
    }

    @Override
    public final boolean suspend() {
        this.isSuspended = true;
        return true;
    }

    /**
     * Return root/starting {@link Activity} object.
     *
     * @return root/starting {@link Activity} object
     */
    public Activity root() {
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

    public Activity getStartingActivity() {
        return startingActivity;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public Condition<ExecutableStatus> getDefaultCondition() {
        return defaultCondition;
    }

    public Workflow setDefaultCondition(Condition<ExecutableStatus> defaultCondition) {
        this.defaultCondition = defaultCondition;
        return this;
    }

    /**
     * Returns {@link Activity} from exact history/execution order. Index starts from 0.
     *
     * @param index order number of {@link Activity} executed before
     * @return {@link Activity}
     */
    public Activity getActivityFromHistoryOrder(int index) {
        return this.activityHistory.get(index);
    }

    /**
     * Add {@link Activity} to history list.
     *
     * @param activity {@link Activity} as history object
     */
    public void addNewHistoryActivity(Activity activity) {
        this.activityHistory.add(activity);
    }
}
