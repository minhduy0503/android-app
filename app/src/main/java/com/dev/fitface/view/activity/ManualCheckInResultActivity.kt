package com.dev.fitface.view.activity

import android.app.Activity
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

        val intent = Intent(this, ManualCheckInActivity::class.java)

        startActivityForResult(intent, REQUEST_CODE_EXAMPLE)
    }

    private val REQUEST_CODE_EXAMPLE = 0x9345


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_EXAMPLE) {

            // resultCode được set bởi DetailActivity
            // RESULT_OK chỉ ra rằng kết quả này đã thành công
            if(resultCode == Activity.RESULT_OK) {
                // Nhận dữ liệu từ Intent trả về
              ToastMessage.makeText(this, "OK", ToastMessage.SHORT, ToastMessage.Type.SUCCESS.type).show()
            } else {
                // DetailActivity không thành công, không có data trả về.
                ToastMessage.makeText(this, "CANCEL", ToastMessage.SHORT, ToastMessage.Type.SUCCESS.type).show()
            }
        }
    }
}