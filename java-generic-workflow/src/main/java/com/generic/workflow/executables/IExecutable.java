package com.generic.workflow.executables;

public interface IExecutable {

    /**
     * Start {@link IExecutable} execution if.
     * @return true if {@link IExecutable} started, false otherwise
     */
    boolean execute();
}
