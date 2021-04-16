package com.dev.fitface.models

import com.google.gson.annotations.SerializedName

data class FaceCheck(
        @SerializedName("avatar")
        var avatar: String,
        @SerializedName("firstname")
        var firstname: String,
        @SerializedName("id")
        var id: Int,
        @SerializedName("lastname")
        var lastname: String,
        @SerializedName("role")
        var role: String,
        @SerializedName("roleid")
        var roleid: Int,
        @SerializedName("shortname")
        var shortname: String,
        @SerializedName("username")
        var username: String
)
