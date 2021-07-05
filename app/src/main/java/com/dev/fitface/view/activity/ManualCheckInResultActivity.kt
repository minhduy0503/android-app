package com.dev.fitface.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.utils.Constants
import com.dev.fitface.view.BaseActivity
import com.dev.fitface.view.customview.ToastMessage
import com.dev.fitface.viewmodel.ManualCheckInResultActivityViewModel
import kotlinx.android.synthetic.main.activity_manual_check_in_result.*


class ManualCheckInResultActivity : BaseActivity<ManualCheckInResultActivityViewModel>() {

  /*  val resultContract = registerForActivityResult(CheckInResult()){ result ->
        if (result.equals("Sucessfully")){
            ToastMessage.makeText(this, "result", ToastMessage.SHORT, ToastMessage.Type.SUCCESS.type).show()
        }else {
            ToastMessage.makeText(this, "NO", ToastMessage.SHORT, ToastMessage.Type.ERROR.type).show()
        }
    }*/
    override fun createViewModel(): ManualCheckInResultActivityViewModel {
        return ViewModelProvider(this).get(ManualCheckInResultActivityViewModel::class.java)
    }

    override fun fetchData() {
        
    }

    override fun handleError(statusCode: Int?, message: String?, bundle: Bundle?) {
        
    }

    override fun observeData() {
        
    }

    override fun setLoadingView(): View? {
        return layoutLoading
    }

    override fun setActivityView() {
        setContentView(R.layout.activity_manual_check_in_result)
    }

    override fun setActivityName(): String {
        return Constants.ActivityName.manualCheckInResultActivity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


}