package com.generic.workflow.library.activities;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.IExecutable;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.conditions.ITestable;
import com.generic.workflow.library.payload.ExecutionPayload;

public final class ActivityLink
        implements IExecutable<ExecutionPayload<?>>, ITestable<Condition<ExecutableStatus>>
{

    private final Condition<ExecutableStatus> linkCondition;
    private final Activity activity;


    public ActivityLink(Condition<ExecutableStatus> linkConditionToExecuteActivity, Activity activityToExecute) {
        this.linkCondition = linkConditionToExecuteActivity;
        this.activity = activityToExecute;
    }

    /**
     * Test if {@link #linkCondition} would result with true or false.
     *
     * @return true if {@link Condition} resulted with true, false otherwise
     */
    public boolean testLink() {
        return this.linkCondition.testAfter(this.activity.status());
    }

    /**
     * Test if input conditionToTest param would result with true or false.
     *
     * @param conditionToTest condition to test on activity status
     * @return true if {@link Condition} resulted with true, false otherwise
     */
    @Override
    public boolean testAfter(Condition<ExecutableStatus> conditionToTest) {
        return conditionToTest.testAfter(this.activity.status());
    }

    /**
     * If {@link #linkCondition} {@link #testLink()} resulted with true, then {@link #activity} could be executed.
     *
     * @return true if {@link #linkCondition} {@link #testLink()} and {@link #activity} execution resulted with true,
     *          false otherwise
     */
    @Override
    public boolean execute(ExecutionPayload<?> payloadInput) throws Exception {
        if (!this.testLink())
            return false;
        return this.activity.execute(payloadInput);
    }

    public Condition<ExecutableStatus> getLinkCondition() {
        return linkCondition;
    }

    public Activity getActivity() {
        return activity;
    }
}
