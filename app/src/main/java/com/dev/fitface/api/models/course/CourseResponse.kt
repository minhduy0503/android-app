package com.dev.fitface.api.models.course

import android.os.Parcelable
import com.dev.fitface.api.models.BaseResponse
import kotlinx.android.parcel.Parcelize

/**
 * Created by Dang Minh Duy on 25,May,2021
 */
class CourseResponse: BaseResponse() {
    var data: List<Course>? = null
}

@Parcelize
data class Course(
        var id: Int?,
        var displayname: String?,
        var enrolledusercount: Int?,
        var startdate: Long?,
        var summary: String?,
        var category: Int?,
        var complete: Boolean?,
        var completionhascriteria: Boolean?,
        var completionusertracked: Boolean?,
        var enablecompletion: Boolean?,
        var enddate: Long?,
        var format: String?,
        var fullname: String?,
        var hidden: Boolean?,
        var idnumber: String?,
        var isfavourite: Boolean?,
        var lang: String?,
        var lastaccess: String?,
        var marker: Int?,
        var overviewfiles: List<String>?,
        var progress: String?,
        var shortname: String?,
        var showgrades: Boolean?,
        var summaryformat: Int?,
        var visible: Int?
) : Parcelable