package com.generic.workflow.library.workflows;

import com.generic.workflow.library.AdvancedExecutable;
import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.conditions.Condition;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;

import java.util.UUID;

@Slf4j
public abstract class Workflow<S extends ExecutableStatus> extends AdvancedExecutable<S> {

    protected String workflowId;
    protected S workflowStatus;

    protected boolean lastPossibleActivityAdded;


    protected Activity<S> startingActivity;


    public Workflow() {
        this.workflowId = UUID.randomUUID().toString();
        this.lastPossibleActivityAdded = false;
    }


    @Override
    public final S status() {
        return this.workflowStatus;
    }

    public final Activity<S> startWith(Activity<S> activity) {

        activity.setParentWorkflow(this);
        this.startingActivity = activity;

        return activity;
    }

    /**
     * Builds {@link Workflow} from multiple given {@link Activity} objects.
     *
     * @return {@link Workflow} of multiple {@link Activity} objects
     */
    public final Workflow<S> build() {
        return this;
    }

    @Override
    public final boolean execute() {
        return this.startingActivity.execute();
    }

    /**
     * Return root/starting {@link Activity} object.
     *
     * @return root/starting {@link Activity} object
     */
    public Activity<S> root() {
        return this.startingActivity;
    }

    public final void setLastPossibleActivity() {
        this.lastPossibleActivityAdded = true;
    }

    public final boolean isLastPossibleActivityAdded() {
        return this.lastPossibleActivityAdded;
    }
}
