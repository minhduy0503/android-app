package com.dev.fitface.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.fitface.R
import com.dev.fitface.api.ApiService
import com.dev.fitface.models.requests.LoginRequest
import com.dev.fitface.models.response.LoginResponse
import com.dev.fitface.ui.CustomProgressDialog
import com.dev.fitface.ui.CustomToast
import com.dev.fitface.utils.SharedPrefs
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var service: ApiService
    private val progressDialog = CustomProgressDialog()

    private var isValidUsername: Boolean = false
    private var isValidPassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupUI()
        initService()
        initListener()
        validateInputChange()
    }


    private fun initListener() {
        btn_login.setOnClickListener {
            if (!isValidUsername || !isValidPassword) {
                showError()
            } else {
                progressDialog.show(this)
                postLogin()
            }
        }

        btn_showHidePass.setOnClickListener {
            if (edt_password.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
                showPassword()
            } else {
                hidePassword()
            }
        }

    }

    private fun initService() {
        service = ApiService.create()
    }

    private fun validateInputChange() {
        edt_username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_errorUsername.visibility = if (tv_errorUsername.visibility == View.VISIBLE && s.isNullOrBlank()) View.VISIBLE else View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                isValidUsername = !s.isNullOrBlank()
            }

        })

        edt_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_errorPassword.visibility = if (tv_errorPassword.visibility == View.VISIBLE && s.isNullOrBlank()) View.VISIBLE else View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                isValidPassword = !s.isNullOrBlank()
            }

        })

    }

    private fun showError() {
        tv_errorUsername.visibility = if (!isValidUsername) View.VISIBLE else View.INVISIBLE
        tv_errorPassword.visibility = if (!isValidPassword) View.VISIBLE else View.INVISIBLE
    }

    private fun hidePassword() {
        img_showHidePass.setImageResource(R.drawable.ic_invisible)
        edt_password.transformationMethod = PasswordTransformationMethod.getInstance()
        edt_password.setSelection(edt_password.length())
    }

    private fun showPassword() {
        img_showHidePass.setImageResource(R.drawable.ic_visible)
        edt_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
        edt_password.setSelection(edt_password.length())
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

    private fun postLogin() {
        val username = edt_username.text.toString()
        val password = edt_password.text.toString()
        val loginRequest = LoginRequest(username, password)

        service.postLogin(loginRequest).enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(call: Call<LoginResponse?>?, response: Response<LoginResponse?>) {
                progressDialog.dialog.dismiss()

                if (response.code() != 200){
                    CustomToast.makeText(applicationContext, "Error", CustomToast.SHORT, CustomToast.ERROR).show()
                }

                else {
                    val body = response.body()
                    val status = body?.status
                    // If status is 200 -> Login successfully
                     if (status == 200) {
                        CustomToast.makeText(applicationContext, "Login successfully", CustomToast.SHORT, CustomToast.SUCCESS).show()
                        // Save token:
                        val token = body.data?.token
                        SharedPrefs.instance.put("Token", token)
                        /**
                         * Get more information here:
                         * */
                        //Go to home screen:
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse?>?, t: Throwable?) {
                CustomToast.makeText(applicationContext, "Error", Toast.LENGTH_LONG, CustomToast.ERROR).show()
            }
        })
    }

}
