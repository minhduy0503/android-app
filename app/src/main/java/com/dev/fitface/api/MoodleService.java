package com.dev.fitface.api;

import com.dev.fitface.models.LoginRequest;
import com.dev.fitface.models.LoginResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MoodleService {
    @POST("/api/login")
    @FormUrlEncoded
    Call<LoginResponse> postLogin(@FieldMap Map<String, String> params);
}
