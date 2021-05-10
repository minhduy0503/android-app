package com.dev.fitface.api.models.response

import com.dev.fitface.api.models.`object`.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(
        @SerializedName("status")
        var status: Int?,
        @SerializedName("message")
        var message: String?,
        @SerializedName("data")
        var data: User?
)