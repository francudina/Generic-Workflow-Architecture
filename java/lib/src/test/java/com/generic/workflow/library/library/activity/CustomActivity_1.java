package com.generic.workflow.library.library.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.library.payload.CustomPayload;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomActivity_1 extends Activity<ExecutableStatus, CustomPayload> {

    @Override
    public boolean test() {
        int a = 1, b = 1;
        return a + b == 2;
    }

    @Override
    public boolean suspend() {
        return true;
    }

    @Override
    public boolean execute(CustomPayload payloadInput) {
        var date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
//        log.info("Executing CustomActivity_1: {}", date);
        return true;
    }

    @Override
    public boolean test(Condition<ExecutableStatus> inputToTest) {
        return false;
    }
}
