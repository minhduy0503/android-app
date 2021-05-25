package com.dev.fitface.api.service

import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.models.campus.CampusResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Dang Minh Duy on 13,May,2021
 */
interface CampusService {

    @GET("campus")
    fun getCampus(@Query("token") token: String): LiveData<ApiResponse<CampusResponse>>

}