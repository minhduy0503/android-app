package com.dev.fitface.api.models.requests

import com.google.gson.annotations.SerializedName

data class LoginRequest(
        @SerializedName("username")
        var username: String,
        @SerializedName("password")
        var password: String,
)