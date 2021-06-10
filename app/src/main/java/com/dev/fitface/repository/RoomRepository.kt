package com.dev.fitface.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.api_utils.AppExecutor
import com.dev.fitface.api.api_utils.LiveDataCallAdapterFactory
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.room.RoomResponse
import com.dev.fitface.api.service.RoomService
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.SharedPrefs
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Dang Minh Duy on 13,May,2021
 */
class RoomRepository constructor(
    val context: Context,
    val base_url: String,
    val appExecutor: AppExecutor
) {
    private var roomService: RoomService
    val suffix = "/api/"
    val moodle = SharedPrefs.instance[Constants.Param.collection, String::class.java] ?: ""
    val token = SharedPrefs.instance[Constants.Param.token, String::class.java] ?: ""

    init {
        val liveDataCallAdapterFactory = LiveDataCallAdapterFactory()
        val callApiClient = AppUtils.createOkHttpClient(context)

        roomService = Retrofit.Builder()
            .baseUrl(base_url + suffix)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(liveDataCallAdapterFactory)
            .client(callApiClient).build()
            .create(RoomService::class.java)
    }

    companion object {
        private var INSTANCE: RoomRepository? = null
        fun instance(context: Context, BASE_URL: String, appExecutors: AppExecutor) = INSTANCE
            ?: RoomRepository(context, BASE_URL, appExecutors).also {
                INSTANCE = it
            }
    }

    fun getRoom(campus: String): LiveData<Resource<RoomResponse>> {
        return object : NetworkBoundResource<RoomResponse>(appExecutor, context, this.base_url) {
            override fun saveCallResult(item: RoomResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<RoomResponse>> {
                return roomService.getRoom(moodle, token, campus)
            }
        }.asLiveData()
    }

}