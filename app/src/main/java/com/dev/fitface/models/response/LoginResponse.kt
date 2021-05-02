package com.dev.fitface.models.response

import com.dev.fitface.models.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(
        @SerializedName("status")
        var status: Int?,
        @SerializedName("message")
        var message: String?,
        @SerializedName("data")
        var data: User?
)