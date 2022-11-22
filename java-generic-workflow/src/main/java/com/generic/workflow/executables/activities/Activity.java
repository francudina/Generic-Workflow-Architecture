package com.generic.workflow.executables.activities;

import com.generic.workflow.executables.AdvancedExecutable;
import com.generic.workflow.executables.ExecutableStatus;
import com.generic.workflow.executables.IExecutable;

import java.util.UUID;

public abstract class Activity<S extends ExecutableStatus, N extends AdvancedExecutable<S>> extends AdvancedExecutable<S> {

    protected String activityId;

    protected S activityStatus;

    protected N previousActivity;
    protected N nextActivity;


    public Activity(N previousActivity, N nextActivity) {
        this.activityId = UUID.randomUUID().toString();
        this.previousActivity = previousActivity;
        this.nextActivity = nextActivity;
    }

    public Activity() {
        this(null, null);
    }

    public void setPreviousActivity(N previousActivity) {
        this.previousActivity = previousActivity;
    }

    public void setNextActivity(N nextActivity) {
        this.nextActivity = nextActivity;
    }

    @Override
    public S status() {
        return this.activityStatus;
    }
}
