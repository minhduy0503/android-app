package com.dev.fitface

import android.app.Application
import com.google.gson.Gson

class MyApp : Application() {


    var gSon: Gson? = null

    override fun onCreate() {
        super.onCreate()
        mSelf = this
        gSon = Gson()
    }

    companion object {
        private var mSelf: MyApp? = null
        fun self(): MyApp? {
            return mSelf
        }
    }
}