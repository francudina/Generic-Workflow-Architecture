package com.generic.workflow.library.library.realtest.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.payload.ExecutionPayload;

import java.util.Set;

public class Training extends Activity {

    public static final String INPUT_EQUIPMENT_SHIRT = "equipmentShirtSize";
    public static final String INPUT_EQUIPMENT_SHORTS = "equipmentShortsSize";

    private final String shirtSize;
    private final String shortsSize;

    public Training(String shirtSize, String shortsSize) {
        this.shirtSize = shirtSize;
        this.shortsSize = shortsSize;
    }


    @Override
    public boolean testBefore(ExecutionPayload<?> inputPayload) {
        var required = Set.of(
                INPUT_EQUIPMENT_SHIRT,
                INPUT_EQUIPMENT_SHORTS
        );
        return testBeforeHelper(required, inputPayload);
    }

    @Override
    public boolean execute(ExecutionPayload<?> inputPayload) {
        this.activityStatus = ExecutableStatus.STARTED;
        var data = inputPayload.toMap();
        this.activityStatus = ExecutableStatus.IN_PROGRESS;

        String shirt = (String) data.get(INPUT_EQUIPMENT_SHIRT);
        String shorts = (String) data.get(INPUT_EQUIPMENT_SHORTS);

        var result = shirt.equals(shirtSize) && shorts.equals(shortsSize);
        this.activityStatus = result? ExecutableStatus.FINISHED : ExecutableStatus.FAILED;

        return result;
    }
}
