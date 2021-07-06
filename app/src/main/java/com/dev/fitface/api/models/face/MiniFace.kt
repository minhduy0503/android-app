package com.dev.fitface.api.models.face

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MiniFace(val bm: String, var isSelected: Boolean = false) : Parcelable