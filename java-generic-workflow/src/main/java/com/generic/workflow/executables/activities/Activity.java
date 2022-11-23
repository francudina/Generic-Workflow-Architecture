package com.generic.workflow.executables.activities;

import com.generic.workflow.executables.AdvancedExecutable;
import com.generic.workflow.executables.ExecutableStatus;

import java.util.UUID;

public abstract class Activity<S extends ExecutableStatus> extends AdvancedExecutable<S> {

    protected String activityId;

    protected S activityStatus;

    protected Activity<S> previousActivity;
    protected Activity<S> nextActivity;


    public Activity(Activity<S> previousActivity, Activity<S> nextActivity) {
        this.activityId = UUID.randomUUID().toString();
        this.previousActivity = previousActivity;
        this.nextActivity = nextActivity;
    }

    public Activity() {
        this(null, null);
    }

    public void setPreviousActivity(Activity<S> previousActivity) {
        this.previousActivity = previousActivity;
    }

    public void setNextActivity(Activity<S> nextActivity) {
        this.nextActivity = nextActivity;
    }

    @Override
    public S status() {
        return this.activityStatus;
    }
}
