package com.generic.workflow.executables.workflows;

import com.generic.workflow.executables.ExecutableStatus;
import com.generic.workflow.executables.activities.Activity;
import com.generic.workflow.executables.activities.ActivityLink;
import com.generic.workflow.executables.conditions.Condition;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class WorkflowBuilder<S extends ExecutableStatus, C extends Condition<S>> {

    private Activity<S, C> starting;

    private C defaultCondition;


    public WorkflowBuilder() {
    }

    public WorkflowBuilder<S, C> startWith(Activity<S, C> activity) {
        if (this.hasStarting()) {
            log.warn("Starting activity ignored because it's already set!");
            return this;
        }

        this.starting = activity;

        return this;
    }

    public WorkflowBuilder<S, C> setOrResetDefaultCondition(C defaultCondition) {
        this.defaultCondition = defaultCondition;
        return this;
    }

    public WorkflowBuilder<S, C> next(Activity<S, C> executeActivity) {
        if (!this.hasDefaultCondition()) {
            log.warn("Activity ignored because default activity condition isn't set!");
            return this;
        }

        return this.next(this.defaultCondition, executeActivity);
    }

    public WorkflowBuilder<S, C> next(C onCondition, Activity<S, C> executeActivity) {
        if (!this.hasStarting()) {
            log.warn("Next activity ignored because starting activity isn't set!");
            return this;
        }

        // todo add chaining mechanism e.g. by activity id, ...
//        this.current.addNextLink(new ActivityLink<>(onCondition, executeActivity));
//        this.current = activity;

        return this;
    }

    public WorkflowBuilder<S, C> when(C whenCondition, Activity<S, C> thenActivity, Activity<S, C> elseActivity) {
        // todo implement
        return this;
    }


    private boolean hasStarting() {
        return this.starting != null;
    }
    private boolean hasDefaultCondition() {
        return this.defaultCondition != null;
    }
}
