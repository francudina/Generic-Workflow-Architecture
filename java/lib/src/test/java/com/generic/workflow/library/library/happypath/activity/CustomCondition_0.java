package com.generic.workflow.library.library.happypath.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.conditions.ITestable;

public class CustomCondition_0 extends Condition<ExecutableStatus> {

    @Override
    public Condition<ExecutableStatus> negate() {
        return new CustomNegatedCondition();
    }

    @Override
    public boolean testAfter(ExecutableStatus inputToTest) {
        return true;
    }
}
