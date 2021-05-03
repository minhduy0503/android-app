package com.dev.fitface.models.response

import com.dev.fitface.models.FaceCheck
import com.google.gson.annotations.SerializedName

data class FaceResponse(
        @SerializedName("status")
        var status: Int,
        @SerializedName("message")
        var message: String,
        @SerializedName("data")
        var data: ArrayList<FaceCheck>
)