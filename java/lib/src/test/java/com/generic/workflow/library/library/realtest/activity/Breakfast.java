package com.generic.workflow.library.library.realtest.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.library.realtest.activity.inputs.BreakFastInput;
import com.generic.workflow.library.payload.ExecutionPayload;

public class Breakfast extends Activity {

    public static final String INPUT_BREAKFAST_DATE = "breakfast_date";

    @Override
    public boolean testBefore(ExecutionPayload<?> inputPayload) {



        return false;
    }

    @Override
    public boolean execute(ExecutionPayload<?> inputPayload) {



        return false;
    }

    @Override
    public boolean testAfter(Condition<ExecutableStatus> inputToTest) {
        return false;
    }
}
