package com.dev.fitface.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.face.*
import com.dev.fitface.api.models.feedback.FeedbackRequest
import com.dev.fitface.api.models.feedback.FeedbackResponse
import com.dev.fitface.repository.FaceRepository
import com.dev.fitface.repository.MoodleRepository

/**
 * Created by Dang Minh Duy on 18,May,2021
 */
class AutoCheckInActivityViewModel constructor(application: Application) : BaseViewModel(application) {

    private val faceRepository: FaceRepository = FaceRepository.instance(application, BASE_URL, appExecutor)
    private val moodleRepository: MoodleRepository = MoodleRepository.instance(application, BASE_URL, appExecutor)


    var faceStr: MediatorLiveData<String>? = MediatorLiveData<String>().also {
        it.value = null
    }

    var foundFace: MediatorLiveData<Face>? = MediatorLiveData<Face>().also {
        it.value = null
    }

    val feedbackResponse = MediatorLiveData<Resource<FeedbackResponse>>()
    fun postFeedback(input: FeedbackRequest) {
        feedbackResponse.addSource(faceRepository.postFeedback(input)) { newData ->
            setResultData<Resource<FeedbackResponse>>(newData)?.let {
                feedbackResponse.value = it
            }
        }
    }

    val findFaceResponse = MediatorLiveData<Resource<FaceResponse>>()
    fun postFindFace(input: FaceRequest) {
        findFaceResponse.addSource(faceRepository.postFindFace(input)) { newData ->
            setResultData<Resource<FaceResponse>>(newData)?.let {
                findFaceResponse.value = it
            }
        }
    }

    val checkInResponse = MediatorLiveData<Resource<CheckInResponse>>()
    fun postCheckIn(id: Int, input: CheckInRequest) {
        checkInResponse.addSource(moodleRepository.postCheckIn(id, input)) { newData ->
            setResultData<Resource<CheckInResponse>>(newData)?.let {
                checkInResponse.value = it
            }
        }
    }
}