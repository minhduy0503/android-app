package com.dev.fitface.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.api_utils.AppExecutor
import com.dev.fitface.api.api_utils.LiveDataCallAdapterFactory
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.report.ReportCheckInResponse
import com.dev.fitface.api.models.report.SessionReportResponse
import com.dev.fitface.api.service.ReportService
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.SharedPrefs
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ReportRepository  constructor(
    val context: Context,
    val base_url: String,
    val appExecutor: AppExecutor
)  {

    private var reportService: ReportService
    val suffix = "/api/"
    val moodle = SharedPrefs.instance[Constants.Param.collection, String::class.java] ?: ""
    val token = SharedPrefs.instance[Constants.Param.token, String::class.java] ?: ""

    init {
        val liveDataCallAdapterFactory = LiveDataCallAdapterFactory()
        val callApiClient = AppUtils.createOkHttpClient(context)

        reportService = Retrofit.Builder()
            .baseUrl(base_url + suffix)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(liveDataCallAdapterFactory)
            .client(callApiClient).build()
            .create(ReportService::class.java)
    }

    companion object {
        private var INSTANCE: ReportRepository? = null
        fun instance(context: Context, BASE_URL: String, appExecutors: AppExecutor) = INSTANCE
            ?: ReportRepository(context, BASE_URL, appExecutors).also {
                INSTANCE = it
            }
    }

    fun getReportByCourseId(id: Int): LiveData<Resource<ReportCheckInResponse>> {
        return object : NetworkBoundResource<ReportCheckInResponse>(appExecutor, context, this.base_url) {
            override fun saveCallResult(item: ReportCheckInResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<ReportCheckInResponse>> {
                return reportService.getReportByCourseId(moodle, token, id)
            }
        }.asLiveData()
    }

    fun getSessionByCourseId(id: Int): LiveData<Resource<SessionReportResponse>> {
        return object : NetworkBoundResource<SessionReportResponse>(appExecutor, context, this.base_url) {
            override fun saveCallResult(item: SessionReportResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<SessionReportResponse>> {
                return reportService.getSessionByCourseId(moodle, token, id)
            }
        }.asLiveData()
    }



}