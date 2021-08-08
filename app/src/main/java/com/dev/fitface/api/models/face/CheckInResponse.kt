package com.dev.fitface.api.models.face

import android.os.Parcelable
import com.dev.fitface.api.models.BaseResponse
import kotlinx.android.parcel.Parcelize

class CheckInResponse : BaseResponse() {
    var data: List<CheckInData>? = null
}

@Parcelize
data class CheckInData(
    var status: Int,
    var message: String,
    var username: String,
) : Parcelable