package com.dev.fitface.api

import android.os.Bundle
import okhttp3.Headers

/**
 * Created by Dang Minh Duy on 09,May,2021
 */
data class Resource<out T>(val status: ApiStatus, val resource: T?, val message: String?, var bundle: Bundle?, var headers: Headers?) {
    companion object {
        fun <T> success(data: T?, headers: Headers?): Resource<T> {
            return Resource(ApiStatus.SUCCESS, data, null, null, headers)
        }

        fun <T> error(msg: String): Resource<T> {
            return Resource(ApiStatus.ERROR, null, msg, null, null)
        }


        fun <T> loading(data: T?): Resource<T> {
            return Resource(ApiStatus.LOADING, data, null, null, null)
        }

    }
}
