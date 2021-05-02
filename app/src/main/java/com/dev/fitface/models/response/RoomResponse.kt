package com.dev.fitface.models.response

import com.dev.fitface.models.Room
import com.google.gson.annotations.SerializedName

data class RoomResponse(
        @SerializedName("status")
        var status: Int,
        @SerializedName("message")
        var message: String,
        @SerializedName("data")
        var data: ArrayList<Room>
)