package com.dev.fitface.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.api_utils.AppExecutor
import com.dev.fitface.api.api_utils.LiveDataCallAdapterFactory
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.campus.CampusResponse
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.api.models.face.FaceResponse
import com.dev.fitface.api.service.CampusService
import com.dev.fitface.api.service.FaceService
import com.dev.fitface.utils.AppUtils
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Dang Minh Duy on 18,May,2021
 */
class FaceRepository constructor(val context: Context, val base_url: String, val appExecutor: AppExecutor) {

    private var faceService: FaceService
    val suffix = "/face/"

    init {
        val liveDataCallAdapterFactory = LiveDataCallAdapterFactory()
        val callApiClient = AppUtils.createOkHttpClient(context)

        faceService = Retrofit.Builder()
                .baseUrl(base_url + suffix)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(liveDataCallAdapterFactory)
                .client(callApiClient).build()
                .create(FaceService::class.java)
    }

    companion object {
        private var INSTANCE: FaceRepository? = null
        fun instance(context: Context, BASE_URL: String, appExecutors : AppExecutor) = INSTANCE
                ?: FaceRepository(context, BASE_URL, appExecutors).also {
                    INSTANCE = it
                }
    }

    fun postCheckIn(roomId: String, token: String, input: FaceRequest): LiveData<Resource<FaceResponse>> {
        return  object : NetworkBoundResource<FaceResponse>(appExecutor, context, this.base_url){
            override fun saveCallResult(item: FaceResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<FaceResponse>> {
                return faceService.postCheckIn(roomId, token, input)
            }
        }.asLiveData()
    }



}