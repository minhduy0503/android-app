package com.dev.fitface.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.face.*
import com.dev.fitface.repository.FaceRepository
import com.dev.fitface.repository.MoodleRepository

class ManualCheckInActivityViewModel constructor(application: Application) :
    BaseViewModel(application) {

    private val faceRepository: FaceRepository = FaceRepository.instance(application, BASE_URL, appExecutor)
    private val moodleRepository: MoodleRepository = MoodleRepository.instance(application, BASE_URL, appExecutor)


    var capturedFace: MediatorLiveData<List<MiniFace>>? = MediatorLiveData<List<MiniFace>>().also {
        it.value = null
    }

    var checkInImage: MediatorLiveData<List<String>>? = MediatorLiveData<List<String>>().also {
        it.value = null
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