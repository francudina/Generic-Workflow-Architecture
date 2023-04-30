package com.generic.workflow.library.library.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.conditions.Condition;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomActivity_2 extends Activity<ExecutableStatus> {

    @Override
    public boolean test() {
        int a = 2, b = 2;
        return a + b == 4;
    }

    @Override
    public boolean suspend() {
        return true;
    }

    @Override
    public boolean execute() {
        var date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
//        log.info("Executing CustomActivity_2: {}", date);
        return true;
    }

    @Override
    public boolean test(Condition<ExecutableStatus> inputToTest) {
        return false;
    }
}
