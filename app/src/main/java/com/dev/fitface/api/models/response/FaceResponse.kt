package com.dev.fitface.api.models.response

import com.dev.fitface.api.models.`object`.FaceCheck
import com.google.gson.annotations.SerializedName

data class FaceResponse(
        @SerializedName("status")
        var status: Int,
        @SerializedName("message")
        var message: String,
        @SerializedName("data")
        var data: ArrayList<FaceCheck>
)