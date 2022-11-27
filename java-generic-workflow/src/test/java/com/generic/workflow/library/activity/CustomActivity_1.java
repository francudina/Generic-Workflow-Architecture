package com.generic.workflow.library.activity;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.conditions.Condition;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
public class CustomActivity_1 extends Activity<ExecutableStatus> {

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
    public boolean execute() {
        var date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        log.info("Executing CustomActivity_1: {}", date);
        return true;
    }

    @Override
    public boolean test(Condition<ExecutableStatus> inputToTest) {
        return false;
    }
}
