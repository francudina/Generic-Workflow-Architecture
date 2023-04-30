package com.generic.workflow.library.library.activity;

import com.generic.workflow.library.ExecutableStatus;

public class CustomNegatedCondition extends CustomCondition_1 {

    @Override
    public boolean test(ExecutableStatus inputToTest) {
        return false;
    }
}
