package com.generic.workflow.executables;

public abstract class AdvancedExecutable<S extends ExecutableStatus> implements IExecutable {

    /**
     * Test if {@link IExecutable} instance could be executed.
     * @return true if could be executed, false otherwise
     */
    public abstract boolean test();

    /**
     * Stop {@link IExecutable} execution if {@link #test()} method returned true and {@link IExecutable} is running.
     * @return true if {@link IExecutable} stopped, false otherwise
     */
    public abstract boolean stop();

    /**
     * Get {@link IExecutable} execution status.
     * @see ExecutableStatus
     */
    public abstract S status();
}
