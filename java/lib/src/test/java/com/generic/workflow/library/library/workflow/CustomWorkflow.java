package com.generic.workflow.library.library.workflow;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.library.payload.CustomPayload;
import com.generic.workflow.library.workflows.Workflow;

public class CustomWorkflow extends Workflow<ExecutableStatus, CustomPayload> {

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
