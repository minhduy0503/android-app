package com.dev.fitface.api;

import com.dev.fitface.models.Attendance;
import com.dev.fitface.models.ListAttendanceResponse;
import com.dev.fitface.models.LoginRequest;
import com.dev.fitface.models.LoginResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoodleService {
    @POST("/api/login")
    @FormUrlEncoded
    Call<LoginResponse> postLogin(@FieldMap Map<String, String> params);

    @GET("/api/get-reports/{id}")
    Call<ListAttendanceResponse> getListAttendances(@Path ("id") String id, @Query("token") String token);
}
