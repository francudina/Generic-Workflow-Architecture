package com.generic.workflow.library;

import com.generic.workflow.library.payload.ExecutionPayload;

public interface IExecutable
        <P extends ExecutionPayload<?>>
{

    /**
     * Start {@link IExecutable} execution.
     * @return true if {@link IExecutable} started, false otherwise
     */
    boolean execute(P inputPayload);
}
