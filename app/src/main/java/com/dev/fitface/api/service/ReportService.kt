package com.dev.fitface.api.service

import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.models.report.ReportCheckInResponse
import com.dev.fitface.api.models.report.SessionReportResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

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
}