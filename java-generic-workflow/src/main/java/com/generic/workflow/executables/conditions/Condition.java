package com.generic.workflow.executables.conditions;

import com.generic.workflow.executables.IExecutable;

public abstract class Condition implements IExecutable {

    /**
     * Test condition and if true, then operation can be executed.
     * @return true if condition is fulfilled, false otherwise
     */
    abstract boolean test();
}
