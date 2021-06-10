package com.dev.fitface.api.models.report

import android.os.Parcelable
import com.dev.fitface.api.models.BaseResponse
import kotlinx.android.parcel.Parcelize

class SessionReportResponse: BaseResponse() {
    var data: ArrayList<Session>? = null
}

@Parcelize
data class Session(
    var id: Int?,
    var sessdate: Long?,
    var duration: Int?,
    var room: String?,
    var campus: String?
) : Parcelable