package com.dev.fitface.api;

import com.dev.fitface.models.FaceRequest
import com.dev.fitface.models.Student
import com.dev.fitface.models.User
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://f376c6f3fbee.ngrok.io"

interface ApiService {
    @POST("/api/login/")
    @FormUrlEncoded
    fun postLogin(@FieldMap param: Map<String, String>): Call<User>


    @POST("/face/checkin/{id}")
    fun postCheckin(
            @Path("id") id: String,
            @Query("token") token: String,
            @Body images: FaceRequest
    ): Call<Student>


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
