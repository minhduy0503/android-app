package com.dev.fitface.api.models.room

import android.os.Parcelable
import com.dev.fitface.api.models.BaseResponse
import kotlinx.android.parcel.Parcelize

/**
 * Created by Dang Minh Duy on 12,May,2021
 */
class RoomResponse: BaseResponse() {
    var data: List<Room>? = null
}

@Parcelize
data class Room(
        var id: Int? = null,
        var name: String? = null,
        var campus: String? = null,
        var isSelected: Int? = 0
): Parcelable