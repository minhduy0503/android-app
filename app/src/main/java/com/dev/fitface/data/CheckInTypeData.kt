package com.dev.fitface.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Dang Minh Duy on 16,May,2021
 */
@Parcelize
class CheckInTypeData(var id: Int, var name: String, var isSelected: Int? = 0) : Parcelable {
    companion object {
        fun getAllType(): ArrayList<CheckInTypeData> {
            val auto = CheckInTypeData(0, "Tự động", 0)
            val manu = CheckInTypeData(1, "Thủ công", 0)
            return arrayListOf(auto, manu)
        }
    }
}