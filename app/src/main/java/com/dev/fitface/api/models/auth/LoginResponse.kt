package com.dev.fitface.api.models.auth

import com.dev.fitface.api.models.BaseResponse
import com.google.gson.annotations.SerializedName

/**
 * Created by Dang Minh Duy on 09,May,2021
 */
class LoginResponse : BaseResponse() {
    var data: User? = null
}

data class User(
        var id: Int?,
        var username: String?,
        var firstname: String?,
        var lastname: String?,
        var roleid: Int?,
        var shortname: String?,
        var token: String?,
        var role: String?,
)