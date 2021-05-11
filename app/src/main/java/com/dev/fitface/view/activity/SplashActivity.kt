package com.dev.fitface.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dev.fitface.R
import com.dev.fitface.utils.AppUtils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            //Navigate to Login Activity:
            AppUtils.startActivityWithResultCode(this, LoginActivity::class.java, null, null)
        }, 5000)
    }
}