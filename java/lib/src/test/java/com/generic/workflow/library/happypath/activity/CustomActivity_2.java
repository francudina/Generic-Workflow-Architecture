package com.generic.workflow.library.happypath.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.payload.ExecutionPayload;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class CustomActivity_2 extends Activity {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean testBefore(ExecutionPayload<?> inputPayload) {
        int a = 2, b = 2;
        return a + b == 4;
    }

    @Override
    public boolean execute(ExecutionPayload<?> payloadInput) {
        var date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        log.info(String.format("Executing CustomActivity_2: %s", date));
        return true;
    }

    @Override
    public boolean testAfter(Condition<ExecutableStatus> inputToTest) {
        return false;
    }
}
