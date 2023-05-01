package com.generic.workflow.library.library.workflow;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.library.payload.CustomPayload_1;
import com.generic.workflow.library.payload.ExecutionPayload;
import com.generic.workflow.library.workflows.Workflow;

public class CustomWorkflow extends Workflow {

    @Override
    public boolean testBefore(ExecutionPayload<?> inputPayload) {
        int a = 1, b = 1;
        return a + b == 2;
    }

    @Override
    public boolean suspend() {
        return true;
    }

    @Override
    public boolean testAfter(Condition<ExecutableStatus> inputToTest) {
        return false;
    }
}
