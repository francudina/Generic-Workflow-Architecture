package com.generic.workflow.executables.workflows;

import com.generic.workflow.executables.AdvancedExecutable;
import com.generic.workflow.executables.ExecutableStatus;
import com.generic.workflow.executables.conditions.Condition;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public abstract class Workflow<S extends ExecutableStatus, C extends Condition<S>> extends AdvancedExecutable<S> {

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
