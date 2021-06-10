package com.dev.fitface.repository

import android.content.Context
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dev.fitface.api.api_utils.*
import com.dev.fitface.api.models.BaseResponse

/**
 * Created by Dang Minh Duy on 10,May,2021
 */
abstract class NetworkBoundResource<Result>
@MainThread constructor(private val appExecutor: AppExecutor, private var base_context: Context, private var base_url: String? = null) {

    private var result = MediatorLiveData<Resource<Result>>()

    init {
        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: Resource<Result>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }


    private fun fetchFromNetwork() {
        setValue(Resource.loading(null))

        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    val processData = processResponse(response)
                    appExecutor.diskIO().execute {
                        saveCallResult(processData)
                    }
                    if(processData is BaseResponse){
                        setValue(Resource.success(processData,response.headers))
/*                        if (processData.status == 200){

                        }*/
                    }
                }

                is ApiEmptyResponse -> {

                }

                is ApiErrorResponse -> {
                    onFetchFailed()
                    setValue(Resource.error(response.errorMessage))
                }

            }
        }
    }

    protected open fun onFetchFailed() {
        // TODO::
    }

    fun asLiveData() = result as LiveData<Resource<Result>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<Result>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: Result)

    @WorkerThread
    protected abstract fun createCall(): LiveData<ApiResponse<Result>>


}