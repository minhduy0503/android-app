package com.dev.fitface.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.api.models.face.FaceResponse
import com.dev.fitface.api.models.feedback.FeedbackRequest
import com.dev.fitface.api.models.feedback.FeedbackResponse
import com.dev.fitface.repository.FaceRepository

/**
 * Created by Dang Minh Duy on 18,May,2021
 */
class AutoCheckInActivityViewModel constructor(application: Application) : BaseViewModel(application) {

    private val faceRepository: FaceRepository = FaceRepository.instance(application, BASE_URL, appExecutor)

    var faceStr: MediatorLiveData<String>? = MediatorLiveData<String>().also {
        it.value = null
    }

    val faceResponse = MediatorLiveData<Resource<FaceResponse>>()
    fun postCheckIn(id: Int, input: FaceRequest) {
        faceResponse.addSource(faceRepository.postCheckIn(id, input)) { newData ->
            setResultData<Resource<FaceResponse>>(newData)?.let {
                faceResponse.value = it
            }
        }
    }

    val feedbackResponse = MediatorLiveData<Resource<FeedbackResponse>>()
    fun postFeedback(input: FeedbackRequest) {
        feedbackResponse.addSource(faceRepository.postFeedback(input)) { newData ->
            setResultData<Resource<FeedbackResponse>>(newData)?.let {
                feedbackResponse.value = it
            }
        }
    }
}