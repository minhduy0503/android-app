package com.dev.fitface.models.requests

import com.google.gson.annotations.SerializedName

data class FaceRequest(
        @SerializedName("images")
        var images: ArrayList<String>,
        @SerializedName("collection")
        var collection: String
)