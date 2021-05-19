package com.dev.fitface.api.service

import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.api.models.face.FaceResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Dang Minh Duy on 18,May,2021
 */
interface FaceService {

    @POST("checkin/{id}")
    fun postCheckIn(
            @Path("id") roomId: String,
            @Query("token") token: String,
            @Body bodyReq: FaceRequest
    ): LiveData<ApiResponse<FaceResponse>>

}