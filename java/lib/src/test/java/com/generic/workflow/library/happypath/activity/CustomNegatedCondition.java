package com.generic.workflow.library.happypath.activity;

import com.generic.workflow.library.ExecutableStatus;

public class CustomNegatedCondition extends CustomCondition_1 {

    @Override
    public boolean testAfter(ExecutableStatus inputToTest) {
        return false;
    }
}
