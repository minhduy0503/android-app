package com.dev.fitface.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dev.fitface.R
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.SharedPrefs
import com.keijumt.passwordview.ActionListener
import kotlinx.android.synthetic.main.activity_passcode.*

class PasscodeActivity : AppCompatActivity() {

    private var passcode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passcode)
        setupCustomKeyboard()
        initCheck()
        initListener()
    }

    private fun initCheck() {
        passcode = SharedPrefs.instance[Constants.Param.passCode, String::class.java, ""]
    }

    private fun initListener() {
        passCodeView.setListener(object : ActionListener {
            override fun onCompleteInput(inputText: String) {
                if (passcode.isNullOrBlank()) {
                    // TODO: Save pass
                    SharedPrefs.instance.put(Constants.Param.passCode, inputText)
                    val intent = Intent()
                    intent.putExtra(Constants.ActivityName.autoCheckInActivity, "Saved")
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    // TODO: Check pass
                    if (passcode == inputText) {
                        passCodeView.correctAnimation()
                        val intent = Intent()
                        intent.putExtra(Constants.ActivityName.autoCheckInActivity, "Ok")
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        passCodeView.incorrectAnimation()
                    }
                }
            }

            override fun onEndJudgeAnimation() {
                passCodeView.reset()
            }

        })

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupCustomKeyboard() {
        text_0.setOnClickListener { passCodeView.appendInputText((it as TextView).text.toString()) }
        text_1.setOnClickListener { passCodeView.appendInputText((it as TextView).text.toString()) }
        text_2.setOnClickListener { passCodeView.appendInputText((it as TextView).text.toString()) }
        text_3.setOnClickListener { passCodeView.appendInputText((it as TextView).text.toString()) }
        text_4.setOnClickListener { passCodeView.appendInputText((it as TextView).text.toString()) }
        text_5.setOnClickListener { passCodeView.appendInputText((it as TextView).text.toString()) }
        text_6.setOnClickListener { passCodeView.appendInputText((it as TextView).text.toString()) }
        text_7.setOnClickListener { passCodeView.appendInputText((it as TextView).text.toString()) }
        text_8.setOnClickListener { passCodeView.appendInputText((it as TextView).text.toString()) }
        text_9.setOnClickListener { passCodeView.appendInputText((it as TextView).text.toString()) }
        text_d.setOnClickListener { passCodeView.removeInputText() }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

}