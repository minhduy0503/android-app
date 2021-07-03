package com.dev.fitface.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.OrientationEventListener
import android.view.View
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.api.models.feedback.FeedbackRequest
import com.dev.fitface.camerax.CameraManager
import com.dev.fitface.interfaces.CameraCallback
import com.dev.fitface.utils.*
import com.dev.fitface.view.BaseActivity
import com.dev.fitface.view.customview.ToastMessage
import com.dev.fitface.view.fragments.CheckInReportFragment
import com.dev.fitface.view.fragments.CheckInResultFragment
import com.dev.fitface.view.fragments.ReportFragment
import com.dev.fitface.viewmodel.AutoCheckInActivityViewModel
import kotlinx.android.synthetic.main.activity_auto_check_in.*
import java.io.ByteArrayOutputStream


class AutoCheckInActivity : BaseActivity<AutoCheckInActivityViewModel>(),
    CheckInResultFragment.OnResultCheckInFragmentInteractionListener,
    ReportFragment.OnReportFragmentInteractionListener, View.OnClickListener {

    private lateinit var cameraManager: CameraManager
    private var mCallback: CameraCallback? = null
    private var roomId: Int? = null
    private var roomName: String? = null
    private var campusName: String? = null
    private var studentTakenId: String? = null

    private var isDone: Boolean? = null
    private val currentApiVersion = 0

    override fun setLoadingView(): View? {
        return null
    }

    override fun setActivityView() {
        setContentView(R.layout.activity_auto_check_in)
    }

    override fun setActivityName(): String {
        return Constants.ActivityName.autoCheckInActivity
    }

    override fun createViewModel(): AutoCheckInActivityViewModel {
        return ViewModelProvider(this).get(AutoCheckInActivityViewModel::class.java)
    }

    override fun fetchData() {
        //Fetch data
    }

    override fun handleError(statusCode: Int?, message: String?, bundle: Bundle?) {
        hideProgressView()
    }

/*
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
*/


    override fun observeData() {
        observeFaceResponseCheckIn()
        observeFaceStr()
        observeFeedback()
    }

    private fun observeFeedback() {
        viewModel.feedbackResponse.observe(this, Observer{
            when(it.resource?.status){
                200 -> {
                    ToastMessage.makeText(this, "Khiếu nại thành công", ToastMessage.SHORT, ToastMessage.Type.SUCCESS.type).show()
                    cameraManager.startCamera()
                }
                else -> {
                    ToastMessage.makeText(this, "Khiếu nại thất bại",ToastMessage.SHORT, ToastMessage.Type.ERROR.type).show()
                    cameraManager.startCamera()
                }
            }
        })
    }

    private fun observeFaceStr() {
        viewModel.faceStr?.observe(this, {
            // Send to fragment
            it?.let {
                val bundle = Bundle()
                val fragment = CheckInResultFragment.newInstance(bundle)
                fragment.isCancelable = false
                fragment.show(
                    supportFragmentManager,
                    Constants.FragmentName.checkInReportFragment
                )
            }
        })
    }

    private fun observeFaceResponseCheckIn() {
        viewModel.faceResponse.observe(this, Observer {
            it?.resource?.data?.let { data ->
                val bundle = Bundle()
                bundle.putParcelableArrayList(Constants.Param.dataSrc, ArrayList(data))
                val fragment = CheckInResultFragment.newInstance(bundle)
                fragment.show(supportFragmentManager, Constants.FragmentName.checkInResultFragment)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initValue()
        initView()
        initListener()
        initCameraManager()
        askPermission()

      /*  val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = flags
            val decorView = window.decorView
            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    decorView.systemUiVisibility = flags
                }
            }
        }*/
    }

    private fun initCameraManager() {
        cameraManager = CameraManager(
            this,
            cameraView,
            this,
            graphicOverlay,
            mCallback,
            Constants.CameraMode.automatic
        )
    }

    private fun initValue() {
        roomId = intent.getIntExtra(Constants.Param.roomId, -1)
        roomName = intent.getStringExtra(Constants.Param.roomName)
        campusName = intent.getStringExtra(Constants.Param.campusName)

        mCallback = object : CameraCallback {
            override fun onFaceCapture(rect: Rect) {
                tvAction.text = "Đang xử lí"
                captureFace(rect)
                Log.i("Debug","OK")
//                cameraManager.stopCamera()
            }

            override fun onFaceSizeNotify(size: FaceSize) {
                when (size) {
                    FaceSize.SMALL -> {
                        tvAction.text = " Vui lòng tiến gần thiết bị hơn"
                    }
                    FaceSize.BIG -> {
                        tvAction.text = "Vui lòng cách xa thiết bị hơn"
                    }
                }
            }

            override fun onFrontFaceNotify() {
                tvAction.text = "Vui lòng nhìn thẳng vào thiết bị"
            }

            override fun onFaceOutsideNotify() {
                tvAction.text = "Vui lòng đặt khuôn mặt vào vùng hiển thị"
            }

            override fun onFaceInvalidNumber() {
                tvAction.text = "Có nhiều hơn một người"
            }

            override fun onFaceNone() {
                tvAction.text = ""
            }

            override fun onEyeNotify(status: EyeStatus) {
                when (status) {
                    EyeStatus.LEFT_EYE_CLOSE -> {
                        tvAction.text = "Vui lòng mở mắt trái"
                    }
                    EyeStatus.RIGHT_EYE_CLOSE -> {
                        tvAction.text = "Vui lòng mở mắt phải"
                    }
                    EyeStatus.ALL_EYES_CLOSE -> {
                        tvAction.text = "Vui lòng mở hai mắt"
                    }
                }
            }

        }
    }

    private fun initView() {
        tvCampus.text = campusName
        tvRoom.text = roomName
    }

    private fun initListener() {
        btnBack.setOnClickListener(this)
        btnLock.setOnClickListener(this)
    }

    private fun captureFace(faceRect: Rect) {
        setOrientationEvent()
        cameraManager.imageCapture.takePicture(
            cameraManager.cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("UnsafeExperimentalUsageError")
                override fun onCaptureSuccess(image: ImageProxy) {
                    image.image?.let {
                        convertImageTpBitmap(it, faceRect)
                    }
                    super.onCaptureSuccess(image)
                }
            })
    }

    private fun convertImageTpBitmap(image: Image, faceRect: Rect) {
        image.imageToBitmap()
            ?.rotateFlipImage(
                cameraManager.rotation,
                cameraManager.isFrontMode()
            )
            ?.let { bitmap ->
                graphicOverlay.processCanvas.drawBitmap(
                    bitmap,
                    0f,
                    bitmap.getBaseYByView(
                        cameraView,
                        cameraManager.isHorizontalMode()
                    ),
                    Paint().apply {
                        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
                    })

//                  Create bitmap and encode to Base64 string
                val left = faceRect.left
                val top = faceRect.top
                val width = faceRect.width()
                val height = faceRect.height()
                var res = Bitmap.createBitmap(bitmap, left, top, width, height)
                var byteArrayOutputStream = ByteArrayOutputStream();
                res.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                var byteArray = byteArrayOutputStream.toByteArray()
                val str64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
                viewModel.faceStr?.postValue(str64)
            }
    }

    private fun setOrientationEvent() {
        val orientationEventListener = object : OrientationEventListener(this as Context) {
            override fun onOrientationChanged(orientation: Int) {
                val rotation: Float = when (orientation) {
                    in 45..134 -> 270f
                    in 135..224 -> 180f
                    in 225..314 -> 90f
                    else -> 0f
                }
                cameraManager.rotation = rotation
            }
        }
        orientationEventListener.enable()
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera()
            } else {
                ToastMessage.makeText(
                    this,
                    "Permissions not granted by the user.",
                    ToastMessage.Type.ERROR.type,
                    ToastMessage.SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }

    @Suppress("DEPRECATION")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBack -> {
                super.onBackPressed()
            }

            R.id.btnLock -> {
                val intent = Intent(this, PasscodeActivity::class.java)
                startActivityForResult(intent, 1)
            }
        }
    }

    override fun onCheckInResultFragmentInteraction(bundle: Bundle?) {
        when (bundle?.getString(Constants.ActivityName.autoCheckInActivity)) {
            Constants.Param.confirm,
            Constants.Param.close -> {
                cameraManager.startCamera()
            }

            Constants.Param.report -> {
                studentTakenId = bundle.getString(Constants.Param.studentId)
                val bundle = Bundle()
                bundle.putString(Constants.Param.studentId, studentTakenId)
                val fragment = ReportFragment.newInstance(bundle)
                fragment.isCancelable = false
                fragment.show(supportFragmentManager, Constants.FragmentName.reportFragment)
            }
        }
        tvAction.text = "Vui lòng đặt khuôn mặt vào vùng hiển thị"
    }

/*
    override fun onCheckInReportFragmentInteraction(bundle: Bundle) {
        when (bundle.getString(Constants.ActivityName.autoCheckInActivity)) {
            Constants.Param.confirm -> {
                // Call api
                callApiPostCheckIn()
            }
            Constants.Param.retry,
            Constants.Param.close -> {
                cameraManager.startCamera()
            }
        }
        tvAction.text = "Vui lòng đặt khuôn mặt vào vùng hiển thị"
    }
*/


    override fun onReportFragmentInteraction(bundle: Bundle) {
        when (bundle.getString(Constants.ActivityName.autoCheckInActivity)) {
            Constants.Param.close -> {
                cameraManager.startCamera()
            }
            Constants.Param.confirm -> {
                val feedBackStudentId = bundle.getString(Constants.Param.studentId) ?: ""
                callApiPostFeedback(feedBackStudentId)
                cameraManager.startCamera()
            }
        }
    }

    private fun callApiPostCheckIn() {
        val faceStr = viewModel.faceStr?.value
        val input = FaceRequest()
        input.images = listOf(faceStr)
        viewModel.postCheckIn(roomId!!, input)
    }

    private fun callApiPostFeedback(studentId: String) {
        val feedbackReq = FeedbackRequest()
        feedbackReq.collection = "CNTT3"
        feedbackReq.image = viewModel.faceStr?.value
        feedbackReq.description = "Missing in face"
        feedbackReq.roomid = roomId
        feedbackReq.userbetaken = studentTakenId
        feedbackReq.usertaken = studentId
        viewModel.postFeedback(feedbackReq)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1) {

            // resultCode được set bởi DetailActivity
            // RESULT_OK chỉ ra rằng kết quả này đã thành công
            if(resultCode == Activity.RESULT_OK) {
                // Nhận dữ liệu từ Intent trả về
                when(data?.getStringExtra(Constants.ActivityName.autoCheckInActivity)){
                    "Ok" -> {
                        if (btnBack.isEnabled){
                            btnBack.isEnabled = false
                            ToastMessage.makeText(
                                this,
                                "Khóa thành công",
                                ToastMessage.SHORT,
                                ToastMessage.Type.SUCCESS.type
                            ).show()
                            imgLock.setImageResource(R.drawable.ic_lock)
                        } else {
                            btnBack.isEnabled = true
                            ToastMessage.makeText(
                                this,
                                "Mở khóa thành công",
                                ToastMessage.SHORT,
                                ToastMessage.Type.SUCCESS.type
                            ).show()
                            imgLock.setImageResource(R.drawable.ic_unlock)
                        }

                    }
                    "Saved" -> {
                        btnBack.isEnabled = false
                        ToastMessage.makeText(
                            this,
                            "Thiết lập khóa thành công",
                            ToastMessage.SHORT,
                            ToastMessage.Type.SUCCESS.type
                        ).show()
                        imgLock.setImageResource(R.drawable.ic_lock)
                    }
                }

            } else {
                ToastMessage.makeText(
                    this,
                    "Thiết lập khóa không thành công",
                    ToastMessage.SHORT,
                    ToastMessage.Type.ERROR.type
                ).show()
            }
        }
    }


    override fun onBackPressed() {
        if (btnBack.isEnabled){
            super.onBackPressed()
        }
    }
}