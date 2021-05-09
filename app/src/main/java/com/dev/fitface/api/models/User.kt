package com.dev.fitface.api.models

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("username")
        var username: String?,
        @SerializedName("firstname")
        var firstname: String?,
        @SerializedName("lastname")
        var lastname: String?,
        @SerializedName("roleid")
        var roleid: Int?,
        @SerializedName("shortname")
        var shortname: String?,
        @SerializedName("token")
        var token: String?,
        @SerializedName("role")
        var role: String?,
)
