package com.generic.workflow.library.workflows;

import com.generic.workflow.library.IExecutable;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.activities.ActivityLink;
import com.generic.workflow.library.payload.ExecutionPayload;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public final class WorkflowExecutionHandler
        implements IExecutable<ExecutionPayload<?>>
{

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final Workflow workflow;

    public WorkflowExecutionHandler(Workflow workflow) {
        this.workflow = workflow;
    }

    /**
     * Execute given {@link Workflow}.
     *
     * @param payloadInput input data for execution
     * @return true if execution succeeded, false otherwise
     */
    @Override
    public boolean execute(ExecutionPayload<?> payloadInput) throws Exception {

        Activity startingActivity = this.workflow.getStartingActivity();
        if (startingActivity == null) {
            log.warning("Workflow Starting activity does not exist, skipping ...");
            return false;
        }

        return this.executeSingleActivity(startingActivity, payloadInput);
    }

    /**
     * Execute single {@link Activity} and trigger next!
     *
     * @param payloadInput input data for execution
     * @param activity {@link Activity} to execute
     * @return true if execution succeeded, false otherwise
     */
    private boolean executeSingleActivity(Activity activity, ExecutionPayload<?> payloadInput) throws Exception {

        // if further execution is suspended, stop next activity from execution
        if (this.workflow.isSuspended())
            return false;

        log.info(String.format("\nTesting activity: %s", activity.getActivityId()));
        boolean testingResult = activity.testBefore(payloadInput);
        log.info(String.format("Activity with id '%s' could execute without errors? %s",
                activity.getActivityId(), testingResult));

        if (!testingResult)
            throw new Exception(String.format(
                    "Execution of activity '%s' would result with failure!",
                    activity.getActivityId()
            ));

        log.info(String.format("Executing activity: %s", activity.getActivityId()));
        boolean result = activity.execute(payloadInput);
        log.info(String.format("Activity with id '%s' executed without errors? %s", activity.getActivityId(), result));

        log.info(String.format("Searching for next activity that matches output status " +
                        "(status: %s) in total of %s next possible options!",
                activity.status(), activity.getNextActivityOptions().size()));

        Activity nextActivity = activity.getNextActivityOptions().stream()
                .filter(option -> option.getLinkCondition().testAfter(activity.status()))
                .map(ActivityLink::getActivity)
                .findFirst()
                .orElse(null);

        boolean foundNext = nextActivity != null;
        String nextActivityId = foundNext? nextActivity.getActivityId() : null;
        log.info(String.format("Next activity (id: %s) that matches activity output status has been found? %s",
                nextActivityId, foundNext));

        if (!foundNext) {
            log.info(String.format("Skipping execution of next activity for current with id: %s\n",
                    activity.getActivityId()));
            return true;
        }

        log.info("Executing next activity ...\n");
        return this.executeSingleActivity(nextActivity, activity.getExecutionResult());
    }
}
