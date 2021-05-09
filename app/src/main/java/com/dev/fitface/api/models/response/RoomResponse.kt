package com.dev.fitface.api.models.response

import com.dev.fitface.api.models.Room
import com.google.gson.annotations.SerializedName

data class RoomResponse(
        @SerializedName("status")
        var status: Int,
        @SerializedName("message")
        var message: String,
        @SerializedName("data")
        var data: ArrayList<Room>
)