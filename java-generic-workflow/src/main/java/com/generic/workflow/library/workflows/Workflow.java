package com.generic.workflow.library.workflows;

import com.generic.workflow.library.AdvancedExecutable;
import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import lombok.extern.slf4j.Slf4j;

import javax.naming.OperationNotSupportedException;
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

    public final Activity<S> startWith(Activity<S> activity) throws OperationNotSupportedException {

        Activity<S> firstActivity = activity;
        // checking if given activity has already another workflow!
        if (activity.hasWorkflow()) {

            // getting root of the given activity to set starting activity!
            firstActivity = activity.root();
            // getting workflow instance to get params!
            var workflow = firstActivity.build();

            // important for part with multiple "when" methods are called!
            this.lastActivityOptionAdded = workflow.lastActivityOptionAdded;
        }

        // setting parent workflow
        firstActivity.setParentWorkflow(this);
        this.startingActivity = firstActivity;

        // getting last element of the workflow to continue adding new elements, if possible!
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
