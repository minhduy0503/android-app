package com.dev.fitface.api.service

import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.api.models.face.FaceResponse
import com.dev.fitface.api.models.feedback.FeedbackRequest
import com.dev.fitface.api.models.feedback.FeedbackResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by Dang Minh Duy on 18,May,2021
 */
interface FaceService {

    @POST("checkin/{id}")
    fun postCheckIn(
        @Header("moodle") moodle: String,
        @Header("Authorization") token: String,
        @Path("id") roomId: Int,
        @Body bodyReq: FaceRequest
    ): LiveData<ApiResponse<FaceResponse>>

    @POST("feedback")
    fun postFeedback(
        @Header("moodle") moodle: String,
        @Header("Authorization") token: String,
        @Body bodyReq: FeedbackRequest
    ): LiveData<ApiResponse<FeedbackResponse>>
}