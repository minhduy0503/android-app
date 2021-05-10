package com.dev.fitface.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Dang Minh Duy on 10,May,2021
 */
abstract class BaseApplication: AppCompatActivity() {
    companion object{
        const val isProduction = false
        const val PERMISSION_REQUIRED = 0
        const val PERMISSION_NORMAL = 1

        var currentAppActivity: AppCompatActivity? = null
    }

    abstract fun setActivityView()
    abstract fun setActivityName(): String
    abstract fun onActivityCreated()

    protected var activityName = ""
    private var activityIsActive = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityName = setActivityName()
        setActivityView()
        onActivityCreated()
    }

}