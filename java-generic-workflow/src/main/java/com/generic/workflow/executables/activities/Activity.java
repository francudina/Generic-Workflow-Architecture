package com.generic.workflow.executables.activities;

import com.generic.workflow.executables.AdvancedExecutable;
import com.generic.workflow.executables.ExecutableStatus;
import com.generic.workflow.executables.conditions.Condition;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class Activity<S extends ExecutableStatus, C extends Condition<S>> extends AdvancedExecutable<S> {

    protected String activityId;
    protected S activityStatus;


    private List<ActivityLink<S, Activity<S, C>, C>> nextActivityOptions;

    private Activity<S, C> previousActivity;
    private Activity<S, C> nextActivity;


    public Activity() {
        this.activityId = UUID.randomUUID().toString();
        this.nextActivityOptions = new LinkedList<>();
    }

    public void addPreviousActivity(Activity<S, C> previousActivity) {
        this.previousActivity = previousActivity;
    }

    public void addNextLink(ActivityLink<S, Activity<S, C>, C> nextActivityLink) {
        this.nextActivityOptions.add(nextActivityLink);
    }

    @Override
    public S status() {
        return this.activityStatus;
    }

    public S getActivityStatus() {
        return activityStatus;
    }
}
