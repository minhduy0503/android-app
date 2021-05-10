package com.dev.fitface.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dev.fitface.BuildConfig
import com.dev.fitface.api.api_utils.ApiStatus
import com.dev.fitface.api.api_utils.AppExecutor
import com.dev.fitface.api.api_utils.Resource

/**
 * Created by Dang Minh Duy on 09,May,2021
 */
abstract class BaseViewModel constructor(application: Application): AndroidViewModel(application){

    var apiStatus = MutableLiveData<Resource<Any>>()
    var appExecutor: AppExecutor
    var BASE_URL: String = ""

    init {
        appExecutor = AppExecutor()
        if(BuildConfig.DEBUG){
            BASE_URL = BuildConfig.BASE_URL_API
        } else {
            BASE_URL = BuildConfig.BASE_URL_API
        }
    }

    fun <T: Any> setResultData(dataApi: Resource<Any>): T? {
        //set status api result
        if(dataApi.status != apiStatus.value?.status) {
            apiStatus.postValue(dataApi)
        }
        if (dataApi.status == ApiStatus.SUCCESS) {
            return dataApi as T
        }
        return null
    }

}