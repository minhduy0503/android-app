package com.dev.fitface.api.service

import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.api.models.face.FaceResponse
import retrofit2.http.*

/**
 * Created by Dang Minh Duy on 18,May,2021
 */
interface FaceService {

    @Headers("Content-Type: application/json")
    @POST("checkin/{id}")
    fun postCheckIn(
        @Path("id") roomId: Int,
        @Query("token") token: String,
        @Body bodyReq: FaceRequest
    ): LiveData<ApiResponse<FaceResponse>>

}