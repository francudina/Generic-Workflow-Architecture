package com.generic.workflow.executables.workflows;

import com.generic.workflow.executables.AdvancedExecutable;
import com.generic.workflow.executables.IExecutable;
import com.generic.workflow.executables.ExecutableStatus;

import java.util.UUID;

public abstract class Workflow<S extends ExecutableStatus> extends AdvancedExecutable<S> {

    protected String workflowId;

    protected S workflowStatus;


    public Workflow() {
        this.workflowId = UUID.randomUUID().toString();
    }


    @Override
    public S status() {
        return this.workflowStatus;
    }
}
