package com.dev.fitface

import android.app.Application
import com.dev.fitface.api.AppExecutor
import com.google.gson.Gson

class FitFaceApp : Application() {

    private var mAppExecutor: AppExecutor? = null
    var gSon: Gson? = null

    override fun onCreate() {
        super.onCreate()
        mSelf = this
        gSon = Gson()
        mAppExecutor = AppExecutor()
    }

    companion object {
        private var mSelf: FitFaceApp? = null
            private set
        fun self(): FitFaceApp? {
            return mSelf
        }

    }
}