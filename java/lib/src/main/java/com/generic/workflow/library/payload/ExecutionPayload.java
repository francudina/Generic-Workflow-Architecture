package com.generic.workflow.library.payload;

import com.generic.workflow.library.utils.ExecutionUtils;

import java.util.Map;

/**
 * Input/Output data container for
 * {@link com.generic.workflow.library.activities.Activity} inputs/results for/from execution.
 *
 * @param <P> generic param used as Input/Output data
 */
public abstract class ExecutionPayload<P> {

    /**
     * Data used for transfer.
     */
    protected P payload;

    public ExecutionPayload(P payload) {
        this.payload = payload;
    }

    /**
     * Method to convert payload to {@link Map} object. Only public fields are considered.
     * @see ExecutionUtils
     *
     * @return {@link Map} containing public fields values as keys
     * from {@link P} class, and values from those fields.
     */
    public Map<String, Object> toMap() {
        return ExecutionUtils.objectToMap(this.payload);
    }
}
