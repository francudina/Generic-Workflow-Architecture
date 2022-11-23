package com.generic.workflow.executables.conditions.types;

import com.generic.workflow.executables.ExecutableStatus;
import com.generic.workflow.executables.conditions.Condition;

public class ExecutionFailedCondition extends Condition<ExecutableStatus> {

    @Override
    public boolean test(ExecutableStatus statusToTest) {
        return statusToTest.equals(ExecutableStatus.FAILED);
    }
}
