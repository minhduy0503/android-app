package com.dev.fitface.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Campus(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("name")
        var name: String? = null,
        var isSelected: Int? = 0
): Parcelable


