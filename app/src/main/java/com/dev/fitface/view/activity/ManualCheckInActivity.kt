package com.dev.fitface.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.View
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.api.models.face.CheckInRequest
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.api.models.face.MiniFace
import com.dev.fitface.camerax.CameraManager
import com.dev.fitface.interfaces.CameraCallback
import com.dev.fitface.utils.*
import com.dev.fitface.view.BaseActivity
import com.dev.fitface.view.fragments.ManualCheckInResultFragment
import com.dev.fitface.view.fragments.ReportFragment
import com.dev.fitface.viewmodel.ManualCheckInActivityViewModel
import kotlinx.android.synthetic.main.activity_manual_check_in.*


class ManualCheckInActivity : BaseActivity<ManualCheckInActivityViewModel>(),
    ManualCheckInResultFragment.OnManualCheckInFragmentInteractionListener, View.OnClickListener {

    private lateinit var cameraManager: CameraManager
    private var mCallback: CameraCallback? = null

    private var faceRectList: ArrayList<Rect>? = ArrayList()
    private var manualCheckInResultFragment: ManualCheckInResultFragment? = null

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
        initCameraManager()
        initListener()
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
        btnDone.setOnClickListener(this)
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

            override fun onFaceCapture(rect: ArrayList<Rect>?) {
                faceRectList = rect
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
        observeCapturedFace()
        observeFindFaceResponse()
        observeSelectedCheckInFace()
        observeCheckInResponse()
    }

    private fun observeCheckInResponse() {
        viewModel.checkInResponse.observe(this, {
            manualCheckInResultFragment?.updateUICheckInResult(it.resource?.status, it.resource?.message)
            val data = it.resource?.data
            data?.forEach { item ->
                if (item.status == 200){
                    viewModel.findFaceResponse.value?.resource?.data?.first { it.username == item.username}?.statusCheckIn = 1
                }else{
                    viewModel.findFaceResponse.value?.resource?.data?.first { it.username == item.username}?.statusCheckIn = 0
                }
            }
        })
    }

    private fun observeSelectedCheckInFace() {
        viewModel.checkInImage?.observe(this, { list  ->
            list?.let {
                callApiPostCheckIn(list)
            }
        })
    }

    private fun observeFindFaceResponse() {
        viewModel.findFaceResponse.observe(this, {
            val bundle = Bundle()
            manualCheckInResultFragment = ManualCheckInResultFragment.newInstance(bundle)
            manualCheckInResultFragment?.isCancelable = false
            manualCheckInResultFragment?.show(
                supportFragmentManager,
                Constants.FragmentName.reportFragment
            )
        })
    }

    private fun observeCapturedFace() {
        viewModel.capturedFace?.observe(this, { list ->
            list?.let {
                if (it.isNotEmpty()) {
                    val collection: ArrayList<String> = ArrayList()
                    it.forEach { miniface ->
                        collection.add(miniface.bm)
                    }
                    callApiPostFindFace(collection)
                }

            }
        })

    }

    override fun setLoadingView(): View? {
        return layoutLoading
    }

    override fun setActivityView() {
        setContentView(R.layout.activity_manual_check_in)
    }

    override fun setActivityName(): String {
        return Constants.ActivityName.manualCheckInActivity
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnDone -> {
                onBackPressed()
            }
            R.id.btnCapture -> {
                captureFace(faceRectList)
            }
        }
    }

    private fun callApiPostFindFace(faceCollection: List<String>?) {
        faceCollection?.let {
            val faceRequest = FaceRequest()
            faceRequest.images = it
            viewModel.postFindFace(faceRequest)
        }
    }

    private fun callApiPostCheckIn(username: List<String>) {
        val input = CheckInRequest()
        input.usernames = username
        viewModel.postCheckIn(roomId!!, input)
    }

    private fun captureFace(faceRect: ArrayList<Rect>?) {
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

    private fun convertImageTpBitmap(image: Image, faceRect: ArrayList<Rect>?) {
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

                val base64Collection: ArrayList<MiniFace> = ArrayList()

                faceRect?.forEach {
                    val left = it.left
                    val top = it.top
                    val width = it.width()
                    val height = it.height()

                    val faceCropped = Bitmap.createBitmap(bitmap, left, top, width, height)
                    val faceStr = faceCropped.toBase64()

                    val miniFace = MiniFace(faceStr)
                    base64Collection.add(miniFace)
                }
                viewModel.capturedFace?.postValue(base64Collection)
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

    override fun onManualCheckInFragmentInteraction(bundle: Bundle?) {

    }
}
