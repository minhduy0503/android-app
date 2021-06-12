package com.dev.fitface.api.service

import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.models.auth.LoginResponse
import com.dev.fitface.api.models.report.*
import retrofit2.http.*

interface ReportService {
    @GET("get-reports/{id}")
    fun getReportByCourseId(
        @Header("moodle") moodle: String,
        @Header("Authorization") token: String,
        @Path("id") id: Int?
    ): LiveData<ApiResponse<ReportCheckInResponse>>

    @GET("sessions/{id}")
    fun getSessionByCourseId(
        @Header("moodle") moodle: String,
        @Header("Authorization") token: String,
        @Path("id") id: Int?
    ): LiveData<ApiResponse<SessionReportResponse>>

    @GET("get-student-reports")
    fun getSessionByStudentId(
        @Header("moodle") moodle: String,
        @Header("Authorization") token: String,
        @Query("username") username: Int?,
        @Query("courseid") id: Int?
    ): LiveData<ApiResponse<SessionReportByStudentIdResponse>>

    @GET("students/{username}")
    fun getStudentInfo(
        @Header("Authorization") token: String,
        @Header("moodle") moodle: String,
        @Path("username") username: String
    ): LiveData<ApiResponse<LoginResponse>>

    @POST("update-attendance-log/{sessionid}")
    fun postUpdateAttendanceLog(
        @Header("Authorization") token: String,
        @Header("moodle") moodle: String,
        @Path("sessionid") sessionid: String,
        @Body req: UpdateLogRequest
    ): LiveData<ApiResponse<UpdateLogResponse>>

}