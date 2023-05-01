package com.generic.workflow.library.realtest.confitions;

import com.generic.workflow.library.ExecutableStatus;
import com.generic.workflow.library.conditions.Condition;

public class DefaultFinishedCondition extends Condition<ExecutableStatus> {

    @Override
    public Condition<ExecutableStatus> negate() {
        return new NegateDefaultFinishedCondition();
    }

    @Override
    public boolean testAfter(ExecutableStatus inputToTest) {
        return inputToTest.equals(ExecutableStatus.FINISHED);
    }


    public static class NegateDefaultFinishedCondition extends Condition<ExecutableStatus> {

        public NegateDefaultFinishedCondition() {
            super(true);
        }

        @Override
        public Condition<ExecutableStatus> negate() {
            return new DefaultFinishedCondition();
        }

        @Override
        public boolean testAfter(ExecutableStatus inputToTest) {
            return !negate().testAfter(inputToTest);
        }
    }
}
