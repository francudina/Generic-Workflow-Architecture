package com.generic.workflow.executables.workflows;

import com.generic.workflow.executables.ExecutableStatus;
import com.generic.workflow.executables.activities.Activity;
import com.generic.workflow.executables.conditions.Condition;

public final class WorkflowBuilder<S extends ExecutableStatus> {

    private Activity<S> starting;
    private Activity<S> current;

    public WorkflowBuilder() {
    }

    public WorkflowBuilder<S> startWith(Activity<S> activity) {

        if (this.hasStarting())
            return this;

        this.starting = activity;
        this.current = this.starting;

        return this;
    }

    public WorkflowBuilder<S> then(Activity<S> activity) {

        if (!this.hasStarting())
            return this.startWith(activity);

        this.current.setNextActivity(activity);
        this.current = activity;

        return this;
    }

    public WorkflowBuilder<S> when(Condition condition, Activity<S> activity) {
        // todo

        return this;
    }


    private boolean hasStarting() {
        return this.starting != null;
    }
}
