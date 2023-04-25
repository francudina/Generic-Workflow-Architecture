package com.generic.workflow.library;

public interface IExecutable {

    /**
     * Start {@link IExecutable} execution.
     * @return true if {@link IExecutable} started, false otherwise
     */
    boolean execute();
}
