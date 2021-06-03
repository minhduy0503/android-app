package com.dev.fitface.api.service

import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.models.course.CourseResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Dang Minh Duy on 25,May,2021
 */
interface CourseService {

    @GET("teacher-schedules")
    fun getTeacherSchedules(@Query("token") token: String): LiveData<ApiResponse<CourseResponse>>
}