package com.generic.workflow.executables.activities;

import com.generic.workflow.executables.AdvancedExecutable;
import com.generic.workflow.executables.ExecutableStatus;
import com.generic.workflow.executables.conditions.Condition;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class Activity<S extends ExecutableStatus, C extends Condition<S>> extends AdvancedExecutable<S> {

    protected String activityId;
    protected S activityStatus;

    private C defaultCondition;

    private List<ActivityLink<S, Activity<S, C>, C>> nextActivityOptions;


    public Activity() {
        this.activityId = UUID.randomUUID().toString();
        this.nextActivityOptions = new LinkedList<>();
    }


    private void addNextLink(ActivityLink<S, Activity<S, C>, C> nextActivityLink) {
        this.nextActivityOptions.add(nextActivityLink);
    }

    @Override
    public S status() {
        return this.activityStatus;
    }


    public Activity<S, C> setOrResetDefaultCondition(C defaultCondition) {
        this.defaultCondition = defaultCondition;
        return this;
    }

    /**
     * Used when there is only one {@link Activity} possible after current (this) {@link Activity}.
     * Method uses default {@link Condition} set by {@link #setOrResetDefaultCondition(Condition)} method.
     *
     * @param executeActivity execute next {@link Activity}
     * @return next {@link Activity} to execute
     */
    public Activity<S, C> next(Activity<S, C> executeActivity) {

        if (!this.hasDefaultCondition()) {
            log.warn("Activity ignored because default activity condition isn't set!");
            return this;
        }

        return this.next(this.defaultCondition, executeActivity);
    }

    /**
     * Used when there is only one {@link Activity} possible after current (this) {@link Activity}.
     * Method uses given {@link Condition} set as param.
     *
     * @param onCondition {@link Condition} which is used to determine next {@link Activity}
     * @param executeActivity {@link Activity} to execute next based on {@link Condition}
     * @return next {@link Activity} to execute
     */
    public Activity<S, C> next(C onCondition, Activity<S, C> executeActivity) {

        // adding link and default condition
        this.addNextLink(new ActivityLink<>(onCondition, executeActivity));

        return executeActivity;
    }

    /**
     * Executing {@link Activity} if {@link Condition} if fulfilled.
     *
     * @param onCondition {@link Condition} which should be fulfilled
     * @param executeActivity {@link Activity} to execute on fulfilled {@link Condition}
     * @return current (this) {@link Activity}
     */
    public Activity<S, C> when(C onCondition, Activity<S, C> executeActivity) {

        this.addNextLink(new ActivityLink<>(onCondition, executeActivity));

        return this;
    }

    /**
     * Executing {@link Activity} if {@link Condition} if fulfilled, otherwise else {@link Activity}
     *
     * @param onCondition {@link Condition} which should be fulfilled
     * @param executeActivity {@link Activity} to execute on fulfilled {@link Condition}
     * @param elseExecuteActivity else {@link Activity} that needs to be executed
     * @return current (this) {@link Activity}
     */
    public Activity<S, C> when(C onCondition, Activity<S, C> executeActivity, Activity<S, C> elseExecuteActivity) {

        this.when(onCondition, executeActivity);
        this.when(onCondition.negate(), elseExecuteActivity);

        return this;
    }


    private boolean hasDefaultCondition() {
        return this.defaultCondition != null;
    }
}
