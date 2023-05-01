package com.generic.workflow.library.library.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.conditions.Condition;

public class CustomCondition_1 extends Condition<ExecutableStatus> {

    @Override
    public <C extends Condition<ExecutableStatus>> C negate() {
        return (C) new CustomNegatedCondition();
    }

    @Override
    public boolean testAfter(ExecutableStatus inputToTest) {
        return true;
    }
}
