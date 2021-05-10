package com.dev.fitface.api.models.response

import com.dev.fitface.api.models.`object`.Campus
import com.google.gson.annotations.SerializedName


/**
 * Created by Dang Minh Duy on 03,May,2021
 */
data class CampusResponse(
        @SerializedName("status")
        var status: Int,
        @SerializedName("message")
        var message: String,
        @SerializedName("data")
        var data: ArrayList<Campus>
)