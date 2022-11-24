package com.generic.workflow.library.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.conditions.Condition;

public class CustomActivity_2 extends Activity<ExecutableStatus> {

    @Override
    public boolean test() {
        int a = 2, b = 2;
        return a + b == 4;
    }

    @Override
    public boolean suspend() {
        return true;
    }

    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public boolean test(Condition<ExecutableStatus> inputToTest) {
        return false;
    }
}
