package com.globalaccelerex.nipmiddleware.messaging;

import com.fasterxml.jackson.annotation.JsonValue;

public enum QueueMode {

    TSQ("TSQ"),
    CALLBACK("CALLBACK");

    private QueueMode(String type ){
        this.type = type;
    }
    private String type;


    @JsonValue
    public String getType() {
        return type;
    }
}
