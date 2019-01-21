package com.atta.eproperty.model;

import com.google.gson.annotations.SerializedName;

public class PropertyResult {

    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;


    public PropertyResult(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
