package com.dev.fitface.ui.activity;

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dev.fitface.R
import com.dev.fitface.api.ApiService
import com.dev.fitface.models.requests.LoginRequest
import com.dev.fitface.models.response.LoginResponse
import com.dev.fitface.ui.CustomToast
import com.dev.fitface.utils.SharedPrefs
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.bgr_dialog_box.*
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
        // Login event
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
        val loginRequest = LoginRequest(username, password)

        service.postLogin(loginRequest).enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(call: Call<LoginResponse?>?, response: Response<LoginResponse?>) {
                if (response.code() != 200) {
                    CustomToast.makeText(applicationContext, "Error", Toast.LENGTH_LONG, CustomToast.ERROR).show()
                } else if (response.code() == 401) {
                    CustomToast.makeText(applicationContext, "Username or password is wrong", Toast.LENGTH_LONG, CustomToast.WARNING).show()
                } else if (response.code() == 402){
                    CustomToast.makeText(applicationContext, "Expired", Toast.LENGTH_LONG, CustomToast.CONFUSING).show()
                }
                else if (response.isSuccessful) {
                    val body = response.body()
                    val status = body?.status

                    // If status is 200 -> Login successfully
                    if (status == 200) {
                        CustomToast.makeText(applicationContext, "Login successfully", CustomToast.SHORT, CustomToast.SUCCESS).show()
                        // Save token:
                        SharedPrefs.instance.put("token", body.data.token)
                        //Go to home screen:
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse?>?, t: Throwable?) {
                CustomToast.makeText(applicationContext, "Đã có lỗi xảy ra", Toast.LENGTH_LONG, CustomToast.ERROR).show()
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

    private fun showDialog(message: String){
        val dialogBuilder = AlertDialog.Builder(this)
        val v = layoutInflater.inflate(R.layout.bgr_dialog_box, null)
        dialogBuilder.setView(v)
        tvdialog.text = message
        val dialog = dialogBuilder.create()
        btn_dialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}

