package com.dev.fitface.api.models.report

import android.os.Parcelable
import com.dev.fitface.api.models.BaseResponse
import kotlinx.android.parcel.Parcelize

/**
 * Created by Dang Minh Duy on 27,May,2021
 */
class ReportByCourseIDResponse : BaseResponse() {
    var data: List<ReportDataByCourseID>? = null
}

@Parcelize
data class ReportDataByCourseID(
        var studentid: Int?,
        var name: String?,
        var email: String?,
        var count: Int?,
        var c: Int?,
        var b: Int?,
        var t: Int?,
        var v: Int?,
        var reports: List<ReportByStudent>
)

@Parcelize
data class ReportByStudent(
        var sessionid: Int?,
        var lesson: Int?,
        var room: String?,
        var campus: String?,
        var timein: Long?,
        var timeout: Long?,
        var statusid: Int?,
        var sessdata: Long?
) : Parcelable