package com.dev.fitface.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.api.models.face.FaceResponse
import com.dev.fitface.api.models.room.Room
import com.dev.fitface.repository.FaceRepository

/**
 * Created by Dang Minh Duy on 18,May,2021
 */
class AutoCheckInActivityViewModel constructor(application: Application) : BaseViewModel(application) {

    private val faceRepository: FaceRepository = FaceRepository.instance(application, BASE_URL, appExecutor)

    var faceStr: MutableLiveData<String>? = MutableLiveData<String>().also {
        it.value = null
    }

    val faceResponse = MediatorLiveData<Resource<FaceResponse>>()
    fun getCampus(roomId: String, token: String, input: FaceRequest) {
        faceResponse.addSource(faceRepository.postCheckIn(roomId, token, input)) { newData ->
            setResultData<Resource<FaceResponse>>(newData)?.let {
                faceResponse.value = it
            }

        }
    }
}