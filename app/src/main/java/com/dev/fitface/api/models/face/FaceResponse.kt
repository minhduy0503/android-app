package com.dev.fitface.api.models.face

import android.os.Parcelable
import com.dev.fitface.api.models.BaseResponse
import kotlinx.android.parcel.Parcelize

/**
 * Created by Dang Minh Duy on 12,May,2021
 */

class FaceResponse : BaseResponse() {
    var data: List<Face>? = null
}

@Parcelize
data class Face(
    var status: Int?,
    var message: String?,
    var id: Int?,
    var username: String?,
    var firstname: String?,
    var lastname: String?,
    var roleid: Int?,
    var role: String?,
    var shortname: String?,
    var userpictureurl: String?,
    @Transient
    var statusCheckIn: Int = -1,
    @Transient
    var isSelected: Boolean = false
) : Parcelable