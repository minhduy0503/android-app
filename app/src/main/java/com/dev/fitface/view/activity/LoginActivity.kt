package com.dev.fitface.view.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.api.models.auth.LoginInput
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.SharedPrefs
import com.dev.fitface.view.BaseActivity
import com.dev.fitface.view.customview.ToastMessage
import com.dev.fitface.view.fragments.CheckInReportFragment
import com.dev.fitface.viewmodel.LoginActivityViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity<LoginActivityViewModel>(), View.OnClickListener {

    private var isValidUsername: Boolean = false
    private var isValidPassword: Boolean = false


    override fun setActivityView() {
        setContentView(R.layout.activity_login)
    }

    override fun setActivityName(): String {
        return Constants.ActivityName.loginActivity
    }

    override fun setLoadingView(): View? {
        return findViewById(R.id.layoutLoading)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
    }

    private fun initListener() {

        // Clear all focus
        loginLayout.setOnClickListener {
            edt_username.clearFocus()
            edt_password.clearFocus()
            AppUtils.hideSoftKeyboard(it)
        }

        // Username, password listen the change
        edt_username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_errorUsername.visibility =
                    if (tv_errorUsername.visibility == View.VISIBLE && s.isNullOrBlank()) View.VISIBLE else View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                isValidUsername = !s.isNullOrBlank()
            }

        })

        edt_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_errorPassword.visibility =
                    if (tv_errorPassword.visibility == View.VISIBLE && s.isNullOrBlank()) View.VISIBLE else View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                isValidPassword = !s.isNullOrBlank()
            }
        })

        // Button listener
        btn_showHidePass.setOnClickListener(this)
        btn_login.setOnClickListener(this)
    }

    override fun createViewModel(): LoginActivityViewModel {
        return ViewModelProvider(this).get(LoginActivityViewModel::class.java)
    }

    override fun fetchData() {
        // Fetch data first if needed
    }

    override fun handleError(statusCode: Int?, message: String?, bundle: Bundle?) {
        ToastMessage.makeText(
            this,
            " Đăng nhập không thành công",
            ToastMessage.SHORT,
            ToastMessage.Type.ERROR.type
        ).show()
    }

    override fun observeData() {
        observeLogin()
    }

    private fun observeLogin() {
        viewModel.responseLogin.observe(this, Observer {
            it.resource?.data.let { user ->
                ToastMessage.makeText(
                    this,
                    " Đăng nhập thành công",
                    ToastMessage.SHORT,
                    ToastMessage.Type.SUCCESS.type
                ).show()
                SharedPrefs.instance.put("Token", user?.token)
                AppUtils.startActivityWithNameAndClearTask(this, MainActivity::class.java)
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                if (!isValidUsername || !isValidPassword) {
                    showError()
                } else {
                    callApiPostLogin()
                }
            }
            R.id.btn_showHidePass -> {
                if (edt_password.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
                    showPassword()
                } else {
                    hidePassword()
                }
            }
        }
    }

    private fun callApiPostLogin() {
        val username = edt_username.text.toString()
        val password = edt_password.text.toString()
        val loginInput = LoginInput(username, password)

        viewModel.postLogin(loginInput)
    }

}
