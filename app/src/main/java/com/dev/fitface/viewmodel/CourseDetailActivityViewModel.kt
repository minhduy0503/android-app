package com.dev.fitface.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.report.ReportCheckIn
import com.dev.fitface.api.models.report.ReportCheckInResponse
import com.dev.fitface.api.models.report.Session
import com.dev.fitface.api.models.report.SessionReportResponse
import com.dev.fitface.repository.ReportRepository
import com.dev.fitface.utils.AppUtils

class CourseDetailActivityViewModel constructor(application: Application): BaseViewModel(application){

    private val reportRepository: ReportRepository = ReportRepository.instance(application, BASE_URL, appExecutor)

    var report: MediatorLiveData<List<ReportCheckIn>?> = MediatorLiveData<List<ReportCheckIn>?>().also {
        it.value = null
    }

    var session: MediatorLiveData<List<Session>?> = MediatorLiveData<List<Session>?>().also {
        it.value = null
    }

    val responseReportByCourseId = MediatorLiveData<Resource<ReportCheckInResponse>>()
    fun getReportByCourseId(id: Int){
        responseReportByCourseId.addSource(reportRepository.getReportByCourseId(id)){ newData ->
            setResultData<Resource<ReportCheckInResponse>>(newData)?.let {
                responseReportByCourseId.value = it
            }
        }
    }

    val responseSessionByCourseId = MediatorLiveData<Resource<SessionReportResponse>>()
    fun getSessionByCourseId(id: Int){
        responseSessionByCourseId.addSource(reportRepository.getSessionByCourseId(id)){ newData ->
            setResultData<Resource<SessionReportResponse>>(newData)?.let {
                responseSessionByCourseId.value = it
            }
        }
    }

    private fun orderReport(data: Resource<SessionReportResponse>){
        var itemIndex: Int? = null
        data.resource?.data
    }
}