package com.dev.fitface.api.models.report

import android.os.Parcelable
import com.dev.fitface.api.models.BaseResponse
import kotlinx.android.parcel.Parcelize

/**
 * Created by Dang Minh Duy on 27,May,2021
 */
class ReportCheckInResponse : BaseResponse() {
    var data: List<ReportCheckIn>? = null
}

@Parcelize
data class ReportCheckIn(
        var studentid: Int?,
        var name: String?,
        var email: String?,
        var count: Int?,
        var c: Int?,
        var b: Int?,
        var t: Int?,
        var v: Int?,
        var reports: List<CheckInInfo>
) : Parcelable

@Parcelize
data class CheckInInfo(
        var sessionid: Int?,
        var lesson: Int?,
        var room: String?,
        var campus: String?,
        var timein: Long?,
        var timeout: Long?,
        var statusid: Int?,
        var sessdata: Long?
) : Parcelable