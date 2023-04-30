package com.generic.workflow.library.activities;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.IExecutable;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.conditions.ITestable;

public class ActivityLink<S extends ExecutableStatus>
        implements IExecutable, ITestable<Condition<S>> {

    private final Condition<S> linkCondition;
    private final Activity<S> activity;


    public ActivityLink(Condition<S> linkConditionToExecuteActivity, Activity<S> activityToExecute) {
        this.linkCondition = linkConditionToExecuteActivity;
        this.activity = activityToExecute;
    }

    /**
     * Test if {@link #linkCondition} would result with true or false.
     *
     * @return true if {@link Condition} resulted with true, false otherwise
     */
    public boolean testLink() {
        return this.linkCondition.test(this.activity.status());
    }

    /**
     * Test if input conditionToTest param would result with true or false.
     *
     * @param conditionToTest condition to test on activity status
     * @return true if {@link Condition} resulted with true, false otherwise
     */
    @Override
    public boolean test(Condition<S> conditionToTest) {
        return conditionToTest.test(this.activity.status());
    }

    /**
     * If {@link #linkCondition} {@link #testLink()} resulted with true, then {@link #activity} could be executed.
     *
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

    public Activity<S> getActivity() {
        return activity;
    }
}
