package com.dev.fitface.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.report.ReportCheckInResponse
import com.dev.fitface.repository.CourseRepository

class CourseDetailActivityViewModel constructor(application: Application): BaseViewModel(application){

    private val courseRepository: CourseRepository = CourseRepository.instance(application, BASE_URL, appExecutor)

    val responseReportByCourseId = MediatorLiveData<Resource<ReportCheckInResponse>>()
    fun getReportByCourseId(id: String, token: String){
        responseReportByCourseId.addSource(courseRepository.getReportByCourseId(id, token)){ newData ->
            setResultData<Resource<ReportCheckInResponse>>(newData)?.let {
                responseReportByCourseId.value = it
            }

        }
    }
}