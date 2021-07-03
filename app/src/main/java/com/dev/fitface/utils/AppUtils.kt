package com.dev.fitface.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dev.fitface.BuildConfig
import com.dev.fitface.R
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Dang Minh Duy on 09,May,2021
 */
class AppUtils {
    companion object {
        fun createOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
                .build()
        }

        private fun getLoggingInterceptor(): Interceptor {
            val interceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                interceptor.level = HttpLoggingInterceptor.Level.NONE
            }
            return interceptor
        }

        fun <T> startActivityWithBundle(
            beginActivity: AppCompatActivity,
            finishActivity: Class<T>, bundle: Bundle?
        ) {
            val intentAcitivity = Intent(beginActivity, finishActivity)
            bundle?.let {
                intentAcitivity.putExtras(it)
            }
            beginActivity.startActivity(intentAcitivity)
            beginActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        fun <T> startActivityWithNameAndClearTask(
            activity: AppCompatActivity,
            desActivity: Class<T>
        ) {
            val intent = Intent(activity, desActivity)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        fun <T> startActivity(activity: AppCompatActivity, desActivity: Class<T>) {
            val intent = Intent(activity, desActivity)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        fun hideSoftKeyboard(view: View) {
            val inputMethodManager: InputMethodManager = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun addFragmentWithAnimLeft(
            fragmentTransaction: FragmentTransaction,
            container: Int,
            fragment: Fragment,
            tag: String
        ) {
            fragmentTransaction
                .setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right
                )
                .addToBackStack(tag)
                .replace(container, fragment)
                .commit()
        }

        fun finishActivityWithAnim(activity: Activity, intent: Intent?) {
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        fun getHourAndMinute(time: Long?): String {
            time?.let {
                val stamp: Long = time.times(1000L)
                val date = Date(stamp)
                val timeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val timeStr: String = timeFormat.format(date)
                return timeStr.substring(11, timeStr.length)
            }
            return ""
        }

        fun getCurrentTime(): Long{
            return System.currentTimeMillis()
        }

        fun isInProgress(time: Long, limit: Long): Boolean{
            val diff = getCurrentTime() - time.times(1000L)
            return diff <= (limit.times(1000L)) && diff >= 0
        }

        fun isInProgressBetweenTimes(time1: Long, time2: Long): Boolean{
            val diff = time1 - time2
            return diff in 0..1800000
        }

        fun isInFuture(time: Long): Boolean{
            return (getCurrentTime() - time.times(1000)) >= 300000
        }

        fun getDate(time: Long): String {
            val stamp: Long = time.times(1000L)
            val date = Date(stamp)
            val timeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val timeStr: String = timeFormat.format(date)
            return timeStr.substring(0, 10)
        }

        fun getDuration(time: Long?): String? {
            if(time != null){
                val hour = time?.div(3600)
                val minute = time?.rem(3600)?.div(60)
                return "${hour}h ${minute}'"
            }
            return ""
        }

        fun getTime(time: Long): String {
            val stamp: Long = time.times(1000L)
            val date = Date(stamp)
            val timeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            return timeFormat.format(date)
        }
    }

}