package com.dev.fitface

import android.app.Application
import com.dev.fitface.api.api_utils.AppExecutor
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson

class FitFaceApp : Application() {

    private var mAppExecutor: AppExecutor? = null
    var gSon: Gson? = null

    override fun onCreate() {
        super.onCreate()
        mSelf = this
        gSon = Gson()
        mAppExecutor = AppExecutor()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }

    companion object {
        private var mSelf: FitFaceApp? = null
        fun self(): FitFaceApp? {
            return mSelf
        }

    }
}