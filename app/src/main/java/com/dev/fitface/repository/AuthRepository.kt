package com.dev.fitface.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.api_utils.AppExecutor
import com.dev.fitface.api.api_utils.LiveDataCallAdapterFactory
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.auth.LoginInput
import com.dev.fitface.api.models.auth.LoginResponse
import com.dev.fitface.api.service.AuthService
import com.dev.fitface.utils.AppUtils
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Dang Minh Duy on 09,May,2021
 */
class AuthRepository constructor(val context: Context, val base_url: String, val appExecutor: AppExecutor){
    private var authService: AuthService
    val suffix = "/api/"

    init {
        val liveDataCallAdapterFactory = LiveDataCallAdapterFactory()
        val callApiClient = AppUtils.createOkHttpClient(context)

        authService = Retrofit.Builder()
                .baseUrl(base_url + suffix)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(liveDataCallAdapterFactory)
                .client(callApiClient).build()
                .create(AuthService::class.java)
    }

    companion object {
        private var INSTANCE: AuthRepository? = null
        fun instance(context: Context, BASE_URL: String, appExecutors : AppExecutor) = INSTANCE
                ?: AuthRepository(context, BASE_URL, appExecutors).also {
                    INSTANCE = it
                }
    }

    fun postLogin(input: LoginInput): LiveData<Resource<LoginResponse>> {
        return  object : NetworkBoundResource<LoginResponse>(appExecutor, context, this.base_url){
            override fun saveCallResult(item: LoginResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<LoginResponse>> {
                return authService.postLogin(input)
            }
        }.asLiveData()
    }

}