package com.dev.fitface.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.auth.LoginResponse
import com.dev.fitface.api.models.auth.User
import com.dev.fitface.api.models.report.*
import com.dev.fitface.repository.ReportRepository
import com.dev.fitface.utils.AppUtils

class CourseDetailActivityViewModel constructor(application: Application) :
    BaseViewModel(application) {

    private val reportRepository: ReportRepository =
        ReportRepository.instance(application, BASE_URL, appExecutor)

    var report: MediatorLiveData<List<ReportCheckIn>?> =
        MediatorLiveData<List<ReportCheckIn>?>().also {
            it.value = null
        }

    var session: MediatorLiveData<List<Session>?> = MediatorLiveData<List<Session>?>().also {
        it.value = null
    }

    var sessionByStudentId: MediatorLiveData<List<ReportCheckIn>?> = MediatorLiveData<List<ReportCheckIn>?>().also {
        it.value = null
    }

    var studentInfo: MediatorLiveData<User?> = MediatorLiveData<User?>().also {
        it.value = null
    }

    val responseReportByCourseId = MediatorLiveData<Resource<ReportCheckInResponse>>()
    fun getReportByCourseId(id: Int) {
        responseReportByCourseId.addSource(reportRepository.getReportByCourseId(id)) { newData ->
            setResultData<Resource<ReportCheckInResponse>>(newData)?.let {
                responseReportByCourseId.value = it
            }
        }
    }

    val responseSessionByCourseId = MediatorLiveData<Resource<SessionReportResponse>>()
    fun getSessionByCourseId(id: Int) {
        responseSessionByCourseId.addSource(reportRepository.getSessionByCourseId(id)) { newData ->
            setResultData<Resource<SessionReportResponse>>(newData)?.let {
                orderSessionReportByCourseId(it)
                responseSessionByCourseId.value = it
            }
        }
    }

    val responseSessionByStudentId = MediatorLiveData<Resource<SessionReportByStudentIdResponse>>()
    fun getSessionByStudentId(username: Int, id: Int) {
        responseSessionByCourseId.addSource(reportRepository.getSessionByStudentId(username, id)) { newData ->
            setResultData<Resource<SessionReportByStudentIdResponse>>(newData)?.let {
//                orderSessionReportByStudentId(it)
                responseSessionByStudentId.value = it
                Log.i("Debug","OKAY")
            }
        }
    }

    val responseStudentInfo = MediatorLiveData<Resource<LoginResponse>>()
    fun getStudentInfo(username: String) {
        responseStudentInfo.addSource(reportRepository.getStudentInfo(username)) { newData ->
            setResultData<Resource<LoginResponse>>(newData)?.let {
                responseStudentInfo.value = it
            }
        }
    }

    val responseUpdateLog = MediatorLiveData<Resource<UpdateLogResponse>>()
    fun postUpdateAttendanceLog(sessionId: String, input: UpdateLogRequest) {
        responseStudentInfo.addSource(reportRepository.postUpdateAttendanceLog(sessionId, input)) { newData ->
            setResultData<Resource<UpdateLogResponse>>(newData)?.let {
                responseUpdateLog.value = it
            }
        }
    }


    private fun orderSessionReportByCourseId(data: Resource<SessionReportResponse>) {
        val newData: ArrayList<Session> = ArrayList(data.resource?.data)
        for (item in newData) {
            if (AppUtils.isInProgress(item.sessdate!!, item.duration!!)) {
                val index = newData.indexOf(item)
                val item = newData.removeAt(index)
                newData.add(0, item)
                break
            }
        }
        data.resource?.data = newData
    }

/*    private fun orderSessionReportByStudentId(data: Resource<SessionReportByStudentIdResponse>) {
        val newData: ArrayList<CheckInInfo> = ArrayList(data.resource?.data?.reports)
        for (item in newData) {
            if (AppUtils.isInProgressBetweenTimes(item.sessdate!!, AppUtils.getCurrentTime())) {
                val index = newData.indexOf(item)
                val item = newData.removeAt(index)
                newData.add(0, item)
                break
            }
        }
        data.resource?.data?.reports = newData
    }*/
}