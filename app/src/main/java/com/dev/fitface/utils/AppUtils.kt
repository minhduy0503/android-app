package com.dev.fitface.utils

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

/**
 * Created by Dang Minh Duy on 09,May,2021
 */
class AppUtils {
    companion object {
        private val TAG = "App_Utils"

        fun createOkHttpClient(context: Context): OkHttpClient {
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

        fun <T> startActivityWithBundle(beginActivity: AppCompatActivity,
                                            finishActivity: Class<T>, bundle: Bundle?) {
            val intentAcitivity = Intent(beginActivity, finishActivity)
            bundle?.let {
                intentAcitivity.putExtras(it)
            }
            beginActivity.startActivity(intentAcitivity)
            beginActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        fun <T> startActivityWithNameAndClearTask(activity: AppCompatActivity, desActivity: Class<T>) {
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

        fun addFragmentWithAnimLeft(fragmentTransaction: FragmentTransaction, container: Int, fragment: Fragment, tag: String) {
          /*  fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, 0, 0)
            fragmentTransaction.replace(container, fragment, tag)
            fragmentTransaction.commit()*/
            fragmentTransaction
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainerView, fragment)
                .commit()
        }
    }

}