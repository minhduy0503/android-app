package com.dev.fitface.api

import com.dev.fitface.models.requests.FaceRequest
import com.dev.fitface.models.requests.LoginRequest
import com.dev.fitface.models.response.CampusResponse
import com.dev.fitface.models.response.FaceResponse
import com.dev.fitface.models.response.LoginResponse
import com.dev.fitface.models.response.RoomResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Query as Query

private const val BASE_URL = "https://fa7adb04e81d.ngrok.io"

interface ApiService {

    @POST("/api/login")
    fun postLogin(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/api/campus")
    fun getCampus(@Query("token") token: String): Call<CampusResponse>

    @GET("/api/rooms")
    fun getRoom(@Query("token") token: String, @Query("campus") campus: String): Call<RoomResponse>




    @Multipart
    @POST("/face/checkin/{id}")
    fun postCheckin(
            @Path("id") id: String,
            @Query("token") token: String,
            @Part images: FaceRequest
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
