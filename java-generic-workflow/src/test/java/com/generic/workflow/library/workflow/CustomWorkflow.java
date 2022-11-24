package com.generic.workflow.library.workflow;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activity.CustomCondition_1;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.workflows.Workflow;

public class CustomWorkflow extends Workflow<ExecutableStatus> {

    @Override
    public boolean test() {
        int a = 1, b = 1;
        return a + b == 2;
    }

    @Override
    public boolean suspend() {
        return true;
    }

    @Override
    public boolean test(Condition<ExecutableStatus> inputToTest) {
        return false;
    }
}
