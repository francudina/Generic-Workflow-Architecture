package com.generic.workflow.library;

import com.generic.workflow.library.conditions.Condition;
import com.generic.workflow.library.conditions.ITestable;
import com.generic.workflow.library.payload.ExecutionPayload;

public abstract class AdvancedExecutable
        implements IExecutable<ExecutionPayload<?>>, ITestable<Condition<ExecutableStatus>>
{

    /**
     * Test if {@link IExecutable} instance can be executed before calling execute.
     * Like checking required data, validating, ...
     *
     * @return true if method could be executed, false otherwise
     */
    public abstract boolean testBefore(ExecutionPayload<?> inputPayload);

    /**
     * Stop with execution if method returns true.
     *
     * @return true if {@link IExecutable} stopped, false otherwise
     */
    public abstract boolean suspend();

    /**
     * Get {@link IExecutable} execution status.
     * @see ExecutableStatus
     */
    public abstract ExecutableStatus status();
}
