package com.dev.fitface.utils

import android.content.Context
import com.dev.fitface.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by Dang Minh Duy on 09,May,2021
 */
class AppUtils {
    companion object {
        private val TAG = "App_Utils"

        fun createOkHttpClient(context: Context): OkHttpClient{
            return OkHttpClient.Builder()
                    .addInterceptor(getLoggingInterceptor())
                    .build()
        }

        private fun getLoggingInterceptor(): Interceptor{
            val interceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                interceptor.level = HttpLoggingInterceptor.Level.NONE
            }
            return interceptor
        }
    }
}