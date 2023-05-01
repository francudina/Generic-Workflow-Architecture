package com.generic.workflow.library.library.payload;

import com.generic.workflow.library.payload.ExecutionPayload;

public class CustomPayload extends ExecutionPayload<String> {

    public CustomPayload(String payload) {
        super(payload);
    }
}
