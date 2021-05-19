package com.dev.fitface.api.models.campus

import android.os.Parcelable
import com.dev.fitface.api.models.BaseResponse
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Dang Minh Duy on 12,May,2021
 */
class CampusResponse : BaseResponse() {
    var data: List<Campus>? = null
}

@Parcelize
data class Campus(
        var id: String?,
        var name: String?,
        @Transient
        var isSelected: Int?
): Parcelable