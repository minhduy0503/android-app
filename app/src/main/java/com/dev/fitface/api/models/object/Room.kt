package com.dev.fitface.api.models.`object`

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room(
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("campus")
        var campus: String? = null,
        var isSelected: Int? = 0
): Parcelable