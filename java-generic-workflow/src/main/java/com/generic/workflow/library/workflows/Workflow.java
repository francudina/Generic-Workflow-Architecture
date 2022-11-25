package com.generic.workflow.library.workflows;

import com.generic.workflow.library.AdvancedExecutable;
import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public abstract class Workflow<S extends ExecutableStatus> extends AdvancedExecutable<S> {

    protected String workflowId;
    protected S workflowStatus;

    protected boolean lastActivityOptionAdded;
    protected boolean startingActivityReturned;

    protected Activity<S> startingActivity;


    public Workflow() {
        this.workflowId = UUID.randomUUID().toString();
        this.lastActivityOptionAdded = false;
        this.startingActivityReturned = false;
    }


    @Override
    public final S status() {
        return this.workflowStatus;
    }

    public final Activity<S> startWith(Activity<S> activity) {

        Activity<S> firstActivity = activity.root();

        if (firstActivity == null)
            firstActivity = activity;

        // TODO update this workflow context/variables from another activity
        //  workflow e.g. set lastActivityOptionAdded
        activity.setParentWorkflow(this);
        this.startingActivity = firstActivity;

        return activity.lastUsableChild();
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
        this.startingActivityReturned = true;
        return this.startingActivity;
    }

    public final void setLastPossibleActivity() {
        this.lastActivityOptionAdded = true;
    }

    public final boolean isLastActivityOptionAdded() {
        return this.lastActivityOptionAdded;
    }

    public final boolean isStartingActivityReturned() {
        return this.startingActivityReturned;
    }
}
