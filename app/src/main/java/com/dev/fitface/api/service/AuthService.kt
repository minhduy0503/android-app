package com.dev.fitface.api.service

import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.models.auth.LoginInput
import com.dev.fitface.api.models.auth.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Dang Minh Duy on 09,May,2021
 */
interface AuthService {
    @POST("login")
    fun postLogin(@Body loginRequest: LoginInput): LiveData<ApiResponse<LoginResponse>>
}