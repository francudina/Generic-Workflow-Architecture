package com.generic.workflow.library.library.realtest.workflow;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.payload.ExecutionPayload;
import com.generic.workflow.library.workflows.Workflow;

public class MorningRoutine extends Workflow {

    @Override
    public boolean testBefore(ExecutionPayload<?> inputPayload) {
        return false;
    }

    @Override
    public boolean testAfter(Condition<ExecutableStatus> inputToTest) {
        return false;
    }
}
