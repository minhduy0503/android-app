package com.dev.fitface.api

import com.dev.fitface.api.models.requests.FaceRequest
import com.dev.fitface.api.models.requests.LoginRequest
import com.dev.fitface.api.models.response.CampusResponse
import com.dev.fitface.api.models.response.FaceResponse
import com.dev.fitface.api.models.response.LoginResponse
import com.dev.fitface.api.models.response.RoomResponse
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://9219358a3313.ngrok.io"

interface ApiService {

    @POST("/api/login")
    fun postLogin(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/api/campus")
    fun getCampus(@Query("token") token: String): Call<CampusResponse>

    @GET("/api/rooms")
    fun getRoom(@Query("token") token: String, @Query("campus") campus: String): Call<RoomResponse>


    @POST("/face/checkin/{id}")
    fun postCheckin(
            @Path("id") id: String,
            @Query("token") token: String,
            @Body bodyReq: FaceRequest
    ): Call<FaceResponse>


    /**
     * Singleton Retrofit
     * */
    companion object{

        private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

        private val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

        fun create(): ApiService{
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            return retrofit.create(ApiService::class.java)
        }
    }

}
