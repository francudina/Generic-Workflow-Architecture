package com.generic.workflow.library.library.realtest.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.payload.ExecutionPayload;

import java.time.Instant;
import java.util.Map;

public class PrepareInformation extends Activity {

    private final Instant currentTime;

    private final String shirtSize;
    private final String shortsSize;

    public PrepareInformation(
            Instant currentTime,
            String shirtSize, String shortsSize
    ) {
        this.currentTime = currentTime;
        this.shirtSize = shirtSize;
        this.shortsSize = shortsSize;
    }


    @Override
    public boolean testBefore(ExecutionPayload<?> inputPayload) {
        return true;
    }

    @Override
    public boolean execute(ExecutionPayload<?> inputPayload) {

        this.activityStatus = ExecutableStatus.IN_PROGRESS;

        this.executionResult = new ExecutionPayload<Map<String, Object>>(
                Map.of(
                        Breakfast.INPUT_DATETIME, currentTime,
                        Training.INPUT_EQUIPMENT_SHIRT, shirtSize,
                        Training.INPUT_EQUIPMENT_SHORTS, shortsSize
                )
        );
        this.activityStatus = ExecutableStatus.FINISHED;

        return true;
    }
}
