package com.dev.fitface.api.models.`object`

import com.google.gson.annotations.SerializedName

data class FaceCheck(
        @SerializedName("status")
        var status: Int,
        @SerializedName("id")
        var id: String,
        @SerializedName("username")
        var username: String,
        @SerializedName("firstname")
        var firstname: String,
        @SerializedName("lastname")
        var lastname: String,
        @SerializedName("roleid")
        var roleid: Int,
        @SerializedName("role")
        var role: String,
        @SerializedName("shortname")
        var shortname: String,
        @SerializedName("avatar")
        var avatar: String
)
