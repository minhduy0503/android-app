package com.dev.fitface.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.media.Image
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.view.OrientationEventListener
import android.view.View
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.api.api_utils.ApiStatus
import com.dev.fitface.api.models.face.CheckInRequest
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.api.models.feedback.FeedbackRequest
import com.dev.fitface.camerax.CameraManager
import com.dev.fitface.interfaces.CameraCallback
import com.dev.fitface.utils.*
import com.dev.fitface.view.BaseActivity
import com.dev.fitface.view.customview.ToastMessage
import com.dev.fitface.view.fragments.CheckInResultFragment
import com.dev.fitface.view.fragments.ReportFragment
import com.dev.fitface.viewmodel.AutoCheckInActivityViewModel
import kotlinx.android.synthetic.main.activity_auto_check_in.*
import java.io.ByteArrayOutputStream


class  AutoCheckInActivity : BaseActivity<AutoCheckInActivityViewModel>(),
    CheckInResultFragment.OnResultCheckInFragmentInteractionListener,
    ReportFragment.OnReportFragmentInteractionListener, View.OnClickListener {

    private lateinit var cameraManager: CameraManager
    private var mCallback: CameraCallback? = null
    private var roomId: Int? = null
    private var roomName: String? = null
    private var campusName: String? = null
    private var studentTakenId: String? = null

    private lateinit var timer: CountDownTimer


    private var checkInResultFragment: CheckInResultFragment? = null

    override fun setLoadingView(): View? {
        return layoutLoading
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

    override fun observeData() {
        observeFaceStr()
        observeFeedback()
        observeFindFaceResponse()
        observeCheckInResponse()
    }

    private fun observeCheckInResponse() {
        viewModel.checkInResponse.observe(this, {
            it?.resource?.data?.let { face ->
                checkInResultFragment?.setResultCheckInMessage(face[0].message, face[0].status)
            }
        })
    }

    private fun observeFindFaceResponse() {
        viewModel.findFaceResponse.observe(this, {
            it?.resource?.data?.let { faces ->
                val bundle = Bundle()
                checkInResultFragment = CheckInResultFragment.newInstance(bundle)
                checkInResultFragment?.isCancelable = false
                checkInResultFragment?.show(
                    supportFragmentManager,
                    Constants.FragmentName.checkInReportFragment
                )
                viewModel.foundFace?.postValue(faces[0])
            }
        })
    }

    private fun observeFeedback() {
        viewModel.feedbackResponse.observe(this, Observer {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    ToastMessage.makeText(
                        this,
                        "Khiếu nại thành công",
                        ToastMessage.SHORT,
                        ToastMessage.Type.SUCCESS.type
                    ).show()
                    cameraManager.startCamera()
                }
                else -> {
                    ToastMessage.makeText(
                        this,
                        "Khiếu nại thất bại",
                        ToastMessage.SHORT,
                        ToastMessage.Type.ERROR.type
                    ).show()
                    cameraManager.startCamera()
                }
            }
        })
    }

    private fun observeFaceStr() {
        viewModel.faceStr?.observe(this, {
            it?.let {
                callApiPostFindFace(it)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
        initValue()
        initView()
        initCameraManager()
        askPermission()
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
                cameraManager.stopCamera()
            }

            override fun onFaceCapture(rect: ArrayList<Rect>?) {

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
                    null)

//                  Create bitmap and encode to Base64 string
                val left = faceRect.left
                val top = faceRect.top
                val width = faceRect.width()
                val height = faceRect.height()
                val res = Bitmap.createBitmap(bitmap, left, top, width, height)
                val resB = Bitmap.createScaledBitmap(res, 160, 160, false)
                val byteArrayOutputStream = ByteArrayOutputStream();
                resB.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                val byteArray = byteArrayOutputStream.toByteArray()
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

    override fun onCheckInResultFragmentInteraction(bundle: Bundle?) {
        when (bundle?.getString(Constants.ActivityName.autoCheckInActivity)) {
            Constants.Param.confirm -> {
                val studentId = bundle.getString(Constants.Param.studentId) ?: "Error"
                callApiPostCheckIn(studentId)
            }

            Constants.Param.retry,
            Constants.Param.startCamera-> {
                cameraManager.startCamera()
            }

            Constants.Param.report -> {
                studentTakenId = bundle.getString(Constants.Param.userTaken)
                val bundle = Bundle()
                bundle.putString(Constants.Param.studentId, studentTakenId)
                val fragment = ReportFragment.newInstance(bundle)
                fragment.isCancelable = false
                fragment.show(supportFragmentManager, Constants.FragmentName.reportFragment)
            }
        }
        tvAction.text = "Vui lòng đặt khuôn mặt vào vùng hiển thị"
    }


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

    private fun callApiPostCheckIn(username: String) {
        val input = CheckInRequest()
        input.usernames = listOf(username)
        viewModel.postCheckIn(roomId!!, input)
    }

    private fun callApiPostFindFace(faceStr: String?) {
        faceStr?.let {
            val faceRequest = FaceRequest()
            faceRequest.images = listOf(it)
            viewModel.postFindFace(faceRequest)
        }
    }

    private fun callApiPostFeedback(studentId: String) {
        val feedbackReq = FeedbackRequest()
        feedbackReq.roomid = roomId
        feedbackReq.userbetaken = studentTakenId
        feedbackReq.usertaken = studentId
        feedbackReq.image = viewModel.faceStr?.value
        feedbackReq.description = "Mistaken in face recognition"

        viewModel.postFeedback(feedbackReq)
    }

    private fun closeDialogTimer(time: Int) {
        timer = object : CountDownTimer(time.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                checkInResultFragment?.dismiss()
                cameraManager.startCamera()
            }
        }
        timer.start()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBack -> {
                super.onBackPressed()
            }
        }
    }
}