package com.dev.fitface.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.api_utils.AppExecutor
import com.dev.fitface.api.api_utils.LiveDataCallAdapterFactory
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.campus.CampusResponse
import com.dev.fitface.api.service.CampusService
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.SharedPrefs
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Dang Minh Duy on 13,May,2021
 */
class CampusRepository constructor(val context: Context, val base_url: String, val appExecutor: AppExecutor){
    private var campusService: CampusService
    val suffix = "/api/"
    val moodle = SharedPrefs.instance[Constants.Param.collection, String::class.java] ?: ""
    val token = SharedPrefs.instance[Constants.Param.token, String::class.java] ?: ""

    init {
        val liveDataCallAdapterFactory = LiveDataCallAdapterFactory()
        val callApiClient = AppUtils.createOkHttpClient()

        campusService = Retrofit.Builder()
                .baseUrl(base_url + suffix)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(liveDataCallAdapterFactory)
                .client(callApiClient).build()
                .create(CampusService::class.java)
    }

    companion object {
        private var INSTANCE: CampusRepository? = null
        fun instance(context: Context, BASE_URL: String, appExecutors : AppExecutor) = INSTANCE
                ?: CampusRepository(context, BASE_URL, appExecutors).also {
                    INSTANCE = it
                }
    }

    fun getCampus(): LiveData<Resource<CampusResponse>> {
        return  object : NetworkBoundResource<CampusResponse>(appExecutor, context, this.base_url){
            override fun saveCallResult(item: CampusResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<CampusResponse>> {
                return campusService.getCampus(moodle,token)
            }
        }.asLiveData()
    }

}