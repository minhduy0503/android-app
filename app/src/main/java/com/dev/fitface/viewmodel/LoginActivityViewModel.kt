package com.dev.fitface.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.auth.LoginInput
import com.dev.fitface.api.models.auth.LoginResponse
import com.dev.fitface.repository.MoodleRepository

/**
 * Created by Dang Minh Duy on 10,May,2021
 */
class LoginActivityViewModel constructor(application: Application): BaseViewModel(application) {

    private val moodleRepository: MoodleRepository = MoodleRepository.instance(application, BASE_URL, appExecutor)

    val responseLogin = MediatorLiveData<Resource<LoginResponse>>()
    fun postLogin(input: LoginInput){
        responseLogin.addSource(moodleRepository.postLogin(input)){ newData ->
            setResultData<Resource<LoginResponse>>(newData)?.let {
                responseLogin.value = it
            }

        }
    }
}