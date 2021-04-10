package com.dev.fitface.ui.activity;

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.dev.fitface.R
import com.dev.fitface.api.ApiService
import com.dev.fitface.models.User
import com.dev.fitface.utils.SharedPrefs
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var service: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupUI()
        initService()
        initListener()
    }

    private fun initListener() {
        /**
         * Login
         * */
        btn_login.setOnClickListener {
            login()
        }
    }

    private fun initService() {
        service = ApiService.create()
    }


    private fun login() {
        val username = edt_username.text.toString()
        val password = edt_password.text.toString()
        val loginRequest = hashMapOf("username" to username, "password" to password)
        val param: Map<String, String> = HashMap(loginRequest)

        service.postLogin(param).enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>?, response: Response<User?>) {
                if (response.isSuccessful) {
                    // Save token:
                    SharedPrefs.instance.put("token", response.body()?.token)

                    //Go to home screen:
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<User?>?, t: Throwable?) {
                Log.e("DEBUG", "Failure call api")
            }
        })
    }

    private fun setupUI() {
        loginLayout.setOnClickListener {
            hideSoftKeyboard(it)
            edt_username.clearFocus()
            edt_password.clearFocus()
        }
    }

    private fun hideSoftKeyboard(view: View) {
        val inputMethodManager: InputMethodManager = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

