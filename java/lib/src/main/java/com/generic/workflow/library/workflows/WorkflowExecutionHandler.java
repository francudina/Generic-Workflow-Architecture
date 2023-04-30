package com.generic.workflow.library.workflows;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.IExecutable;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.activities.ActivityLink;

import java.util.logging.Logger;

public class WorkflowExecutionHandler<S extends ExecutableStatus> implements IExecutable {

    private final Logger log = Logger.getLogger(this.getClass().getName());

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
            log.warning("Workflow Starting activity does not exist, skipping ...");
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

        log.info(String.format("Testing activity: %s", activity.getActivityId()));
        boolean testingResult = activity.test();
        log.info(String.format("Activity with id '%s' could execute without errors? %s",
                activity.getActivityId(), testingResult));

        log.info(String.format("Executing activity: %s", activity.getActivityId()));
        boolean result = activity.execute();
        log.info(String.format("Activity with id '%s' executed without errors? %s", activity.getActivityId(), result));

        log.info(String.format("Searching for next activity that matches output status " +
                        "(status: %s) in total of %s next possible options!",
                activity.status(), activity.getNextActivityOptions().size()));

        Activity<S> nextActivity = activity.getNextActivityOptions().stream()
                .filter(option -> option.getLinkCondition().test(activity.status()))
                .map(ActivityLink::getActivity)
                .findFirst()
                .orElse(null);

        boolean foundNext = nextActivity != null;
        String nextActivityId = foundNext? nextActivity.getActivityId() : null;
        log.info(String.format("Next activity (id: %s) that matches activity output status has been found? %s",
                nextActivityId, foundNext));

        if (!foundNext) {
            log.info(String.format("Skipping execution of next activity for current with id: %s",
                    activity.getActivityId()));
            return true;
        }

        log.info("Executing next activity ...");
        return this.executeSingleActivity(nextActivity);
    }
}
