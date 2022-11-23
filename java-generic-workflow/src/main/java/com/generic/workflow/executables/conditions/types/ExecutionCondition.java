package com.generic.workflow.executables.conditions.types;

import com.generic.workflow.executables.ExecutableStatus;
import com.generic.workflow.executables.conditions.Condition;

public class ExecutionCondition extends Condition<ExecutableStatus> {

    private final ExecutableStatus status;

    public ExecutionCondition(ExecutableStatus status) {
        this.status = status;
    }

    @Override
    public boolean test(ExecutableStatus statusToTest) {
        return statusToTest.equals(ExecutableStatus.FAILED);
    }

    public boolean test() {
        return status.equals(ExecutableStatus.FAILED);
    }
}
