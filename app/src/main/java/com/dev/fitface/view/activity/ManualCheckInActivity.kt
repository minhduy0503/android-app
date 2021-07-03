package com.dev.fitface.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.camerax.CameraManager
import com.dev.fitface.interfaces.CameraCallback
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.EyeStatus
import com.dev.fitface.utils.FaceSize
import com.dev.fitface.view.BaseActivity
import com.dev.fitface.viewmodel.ManualCheckInActivityViewModel
import kotlinx.android.synthetic.main.activity_manual_check_in.*


class ManualCheckInActivity : BaseActivity<ManualCheckInActivityViewModel>(), View.OnClickListener {

    private lateinit var cameraManager: CameraManager
    private var mCallback: CameraCallback? = null
    private var roomId: Int? = null
    private var roomName: String? = null
    private var campusName: String? = null

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initValue()
        initView()
        initListener()
        initCameraManager()
        askPermission()
    }

    private fun askPermission() {
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun initCameraManager() {
        cameraManager = CameraManager(
            this,
            cameraView,
            this,
            graphicOverlay,
            mCallback,
            Constants.CameraMode.manual
        )
    }

    private fun initListener() {
        btnClose.setOnClickListener(this)
        btnCapture.setOnClickListener(this)
    }

    private fun initView() {
        
    }

    private fun initValue() {
        roomId = intent.getIntExtra(Constants.Param.roomId, -1)
        roomName = intent.getStringExtra(Constants.Param.roomName)
        campusName = intent.getStringExtra(Constants.Param.campusName)

        mCallback = object : CameraCallback {
            override fun onFaceCapture(rect: Rect) {

            }

            override fun onFaceSizeNotify(size: FaceSize) {
            }

            override fun onFrontFaceNotify() {
            }

            override fun onFaceOutsideNotify() {
            }

            override fun onFaceInvalidNumber() {
            }

            override fun onFaceNone() {
            }

            override fun onEyeNotify(status: EyeStatus) {
            }

        }
    }

    override fun createViewModel(): ManualCheckInActivityViewModel {
        return ViewModelProvider(this).get(ManualCheckInActivityViewModel::class.java)
    }

    override fun fetchData() {
        
    }

    override fun handleError(statusCode: Int?, message: String?, bundle: Bundle?) {
        
    }

    override fun observeData() {
        
    }

    override fun setLoadingView(): View? {
        return null
    }

    override fun setActivityView() {
        setContentView(R.layout.activity_manual_check_in)
    }

    override fun setActivityName(): String {
        return Constants.ActivityName.manualCheckInActivity
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnClose -> {
                val data = Intent()

                // Truyền data vào intent

                // Truyền data vào intent
                data.putExtra("EXTRA_DATA", "Some interesting data!")

                // Đặt resultCode là Activity.RESULT_OK to
                // thể hiện đã thành công và có chứa kết quả trả về

                // Đặt resultCode là Activity.RESULT_OK to
                // thể hiện đã thành công và có chứa kết quả trả về
                setResult(RESULT_OK, data)

                // gọi hàm finish() để đóng Activity hiện tại và trở về MainActivity.

                // gọi hàm finish() để đóng Activity hiện tại và trở về MainActivity.
                finish()
            }
        }
    }
}