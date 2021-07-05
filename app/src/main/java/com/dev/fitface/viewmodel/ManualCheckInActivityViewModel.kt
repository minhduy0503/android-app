package com.dev.fitface.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.api.models.face.FaceResponse
import com.dev.fitface.repository.FaceRepository

class ManualCheckInActivityViewModel constructor(application: Application) :
    BaseViewModel(application) {

    private val faceRepository: FaceRepository = FaceRepository.instance(application, BASE_URL, appExecutor)

    var foundFace: MediatorLiveData<List<String>>? = MediatorLiveData<List<String>>().also {
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

}