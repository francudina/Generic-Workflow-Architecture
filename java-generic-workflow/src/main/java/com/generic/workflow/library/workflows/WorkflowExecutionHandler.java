package com.generic.workflow.library.workflows;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.IExecutable;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.activities.ActivityLink;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkflowExecutionHandler<S extends ExecutableStatus> implements IExecutable {

    private final Workflow<S> workflow;

    public WorkflowExecutionHandler(Workflow<S> workflow) {
        this.workflow = workflow;
    }

    /**
     * Execute given {@link Workflow}.
     *
     * @return true if execution succeeded, false otherwise
     */
    @Override
    public boolean execute() {

        Activity<S> startingActivity = this.workflow.getStartingActivity();
        if (startingActivity == null) {
            log.warn("Workflow Starting activity does not exist, skipping ...");
            return false;
        }

        return this.executeSingleActivity(startingActivity);
    }

    /**
     * Execute single {@link Activity} an trigger next!
     *
     * @param activity {@link Activity} to execute
     * @return true if execution succeeded, false otherwise
     */
    private boolean executeSingleActivity(Activity<S> activity) {

        log.info("Testing activity: {}", activity.getActivityId());
        boolean testingResult = activity.test();
        log.info("Activity with id '{}' could execute without errors? {}", activity.getActivityId(), testingResult);

        log.info("Executing activity: {}", activity.getActivityId());
        boolean result = activity.execute();
        log.info("Activity with id '{}' executed without errors? {}", activity.getActivityId(), result);

        log.info("Searching for next activity that matches output status (status: {}) in total of {} next possible options!",
                activity.status(), activity.getNextActivityOptions().size());

        Activity<S> nextActivity = activity.getNextActivityOptions().stream()
                .filter(option -> option.getLinkCondition().test(activity.status()))
                .map(ActivityLink::getActivity)
                .findFirst()
                .orElse(null);

        boolean foundNext = nextActivity != null;
        String nextActivityId = foundNext? nextActivity.getActivityId() : null;
        log.info("Next activity (id: {}) that matches activity output status has been found? {}", nextActivityId, foundNext);

        if (!foundNext) {
            log.info("Skipping execution of next activity for current with id: {}", activity.getActivityId());
            return true;
        }

        log.info("Executing next activity ...");
        return this.executeSingleActivity(nextActivity);
    }
}
