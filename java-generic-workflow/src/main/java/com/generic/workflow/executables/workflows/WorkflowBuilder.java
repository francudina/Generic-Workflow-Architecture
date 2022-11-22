package com.generic.workflow.executables.workflows;

import com.generic.workflow.executables.activities.Activity;
import com.generic.workflow.executables.conditions.Condition;

public final class WorkflowBuilder<A extends Activity> {

    private A starting;
    private A current;


    private WorkflowBuilder(A starting, A current) {
        this.starting = starting;
        this.current = current;
    }

    private WorkflowBuilder() {
    }


    public static WorkflowBuilder init() {
        return new WorkflowBuilder();
    }

    public WorkflowBuilder startWith(A activity) {

        if (this.hasStarting())
            return this;

        this.starting = activity;
        this.current = this.starting;

        return this;
    }

    public WorkflowBuilder then(A activity) {

        if (!this.hasStarting())
            return this.startWith(activity);

        this.current.setNextActivity(activity);
        this.current = activity;

        return this;
    }

    public WorkflowBuilder when(Condition condition, A activity) {
        // todo

        return this;
    }


    private boolean hasStarting() {
        return this.starting != null;
    }
}
