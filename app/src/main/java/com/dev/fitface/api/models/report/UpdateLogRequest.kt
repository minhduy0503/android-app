package com.dev.fitface.api.models.report

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class UpdateLogRequest(
    var students: List<Student>? = null
)

@Parcelize
data class Student(
    var username: String?,
    var timein: Long?,
    var timeout: Long?,
    var statusid: Int?,
) : Parcelable