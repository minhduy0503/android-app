package com.dev.fitface.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String BASE_URL = "http://18906e7dd2dc.ngrok.io";
    public static Retrofit retrofit;

    public static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    public static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build();

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }

    public static MoodleService initService(){
        return RetrofitClient.getInstance().create(MoodleService.class);
    }
}
