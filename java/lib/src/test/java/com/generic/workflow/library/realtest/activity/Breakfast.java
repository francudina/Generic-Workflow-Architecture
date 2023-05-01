package com.generic.workflow.library.realtest.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.payload.ExecutionPayload;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;

public class Breakfast extends Activity {

    public static final String INPUT_DATETIME = "currentDateTime";

    private final int startHours;
    private final int endHours;

    public Breakfast(int breakfastStart, int breakfastEnd) {
        this.startHours = breakfastStart;
        this.endHours = breakfastEnd;
    }


    @Override
    public boolean testBefore(ExecutionPayload<?> inputPayload) {
        var required = Set.of(
                INPUT_DATETIME
        );
        return testBeforeHelper(required, inputPayload);
    }

    @Override
    public boolean execute(ExecutionPayload<?> inputPayload) {
        this.activityStatus = ExecutableStatus.STARTED;
        var data = inputPayload.toMap();
        this.activityStatus = ExecutableStatus.IN_PROGRESS;

        Instant timestamp = (Instant) data.get(INPUT_DATETIME);
        var hours = timestamp.atZone(ZoneOffset.UTC).getHour();

        var result = startHours <= hours && hours <= endHours;
        this.activityStatus = result? ExecutableStatus.FINISHED : ExecutableStatus.FAILED;

        return result;
    }
}
