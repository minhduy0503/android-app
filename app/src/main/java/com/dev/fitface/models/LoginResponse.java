package com.dev.fitface.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse extends BaseResponse {
    @SerializedName("data")
    UserInfo data;

    public LoginResponse(String message, int status) {
        super(message, status);
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
