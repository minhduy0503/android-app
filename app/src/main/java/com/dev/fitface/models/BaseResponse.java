package com.dev.fitface.models;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("message")
    String message;
    @SerializedName("status")
    int status;

    public BaseResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
