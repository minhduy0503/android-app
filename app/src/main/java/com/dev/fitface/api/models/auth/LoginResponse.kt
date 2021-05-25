package com.dev.fitface.api.models.auth

import android.os.Parcelable
import com.dev.fitface.api.models.BaseResponse
import kotlinx.android.parcel.Parcelize

/**
 * Created by Dang Minh Duy on 09,May,2021
 */
class LoginResponse : BaseResponse() {
    var data: User? = null
}

@Parcelize
data class User(
        var id: Int?,
        var username: String?,
        var firstname: String?,
        var lastname: String?,
        var roleid: Int?,
        var shortname: String?,
        var token: String?,
        var role: String?,
        var isAdmin: Boolean?,
        var userpictureurl: String?
):Parcelable