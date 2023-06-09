package com.generic.workflow.library.happypath.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.conditions.Condition;

public class CustomCondition_1 extends Condition<ExecutableStatus> {

    @Override
    public Condition<ExecutableStatus> negate() {
        return new CustomNegatedCondition();
    }

    @Override
    public boolean testAfter(ExecutableStatus inputToTest) {
        return true;
    }
}
