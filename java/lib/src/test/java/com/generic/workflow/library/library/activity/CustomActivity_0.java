package com.generic.workflow.library.library.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.payload.ExecutionPayload;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class CustomActivity_0 extends Activity {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean testBefore(ExecutionPayload<?> inputPayload) {
        int a = 1, b = 1;
        return a + b == 2;
    }

    @Override
    public boolean suspend() {
        return true;
    }

    @Override
    public boolean execute(ExecutionPayload<?> payloadInput) {
        var date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        log.info(String.format("Executing CustomActivity_0: %s", date));
        return true;
    }

    @Override
    public boolean testAfter(Condition<ExecutableStatus> inputToTest) {
        return false;
    }
}
