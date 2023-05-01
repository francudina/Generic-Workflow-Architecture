package com.generic.workflow.library.library.realtest.workflow;

import com.generic.workflow.library.payload.ExecutionPayload;
import com.generic.workflow.library.workflows.Workflow;

public class MorningRoutine extends Workflow {

    @Override
    public boolean testBefore(ExecutionPayload<?> inputPayload) {
        return true;
    }
}
