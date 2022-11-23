package com.generic.workflow.executables.activities;

import com.generic.workflow.executables.ExecutableStatus;
import com.generic.workflow.executables.IExecutable;
import com.generic.workflow.executables.conditions.Condition;
import com.generic.workflow.executables.conditions.ITestable;
import com.generic.workflow.executables.conditions.types.ExecutionCondition;

public class ActivityLink<S extends ExecutableStatus, A extends Activity<S, C>, C extends Condition<S>>
        implements IExecutable, ITestable<Condition<S>> {

    private final C linkCondition;
    private final A activity;

    public ActivityLink(C linkConditionToExecuteActivity, A activityToExecute) {
        this.linkCondition = linkConditionToExecuteActivity;
        this.activity = activityToExecute;
    }

    /**
     * Test if {@link #linkCondition} would result with true or false.
     * @return true if {@link Condition} resulted with true, false otherwise
     */
    public boolean testLink() {
        return this.linkCondition.test(this.activity.getActivityStatus());
    }

    /**
     * Test if input conditionToTest param would result with true or false.
     * @param conditionToTest condition to test on activity status
     * @return true if {@link Condition} resulted with true, false otherwise
     */
    @Override
    public boolean test(Condition<S> conditionToTest) {
        return conditionToTest.test(this.activity.getActivityStatus());
    }

    /**
     * If {@link #linkCondition} {@link #testLink()} resulted with true, then {@link #activity} could be executed.
     * @return true if {@link #linkCondition} {@link #testLink()} and {@link #activity} execution resulted with true,
     *          false otherwise
     */
    @Override
    public boolean execute() {
        if (!this.testLink())
            return false;
        return this.activity.execute();
    }

    public Condition<S> getLinkCondition() {
        return linkCondition;
    }

    public Activity<S, C> getActivity() {
        return activity;
    }
}
