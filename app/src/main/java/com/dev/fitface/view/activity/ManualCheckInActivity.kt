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
import androidx.activity.result.contract.ActivityResultContract
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.camerax.CameraManager
import com.dev.fitface.interfaces.CameraCallback
import com.dev.fitface.utils.*
import com.dev.fitface.view.BaseActivity
import com.dev.fitface.viewmodel.ManualCheckInActivityViewModel
import kotlinx.android.synthetic.main.activity_manual_check_in.*


class ManualCheckInActivity : BaseActivity<ManualCheckInActivityViewModel>(), View.OnClickListener {

    private lateinit var cameraManager: CameraManager
    private var mCallback: CameraCallback? = null

    var bigBitmap: ArrayList<String>? = null
    private var faceRectList: ArrayList<Rect>? = ArrayList()
    var mapMiniFace: MutableMap<String, ArrayList<String>>? = mutableMapOf()

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
        when (v?.id) {
            R.id.btnDone -> {
                val data = Intent()
                setResult(RESULT_OK, data)
                finish()
            }
            R.id.btnCapture -> {
                captureFace(faceRectList)
            }
        }
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

                val base64 = bitmap.toBase64()
                val miniFaces: ArrayList<String>? = null

                faceRect?.forEach {
                    val left = it.left
                    val top = it.top
                    val width = it.width()
                    val height = it.height()

                    val faceCropped = Bitmap.createBitmap(bitmap, left, top, width, height)

                    val faceStr = faceCropped.toBase64()
                    miniFaces?.add(faceStr)
                }

                bigBitmap?.add(base64)
                miniFaces?.let {
                    mapMiniFace?.put(base64, it)
                }
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




    inner class CheckInResult : ActivityResultContract<Int, MiniFaceCollection?>() {
        override fun createIntent(context: Context, input: Int?): Intent {
            return Intent(context, ManualCheckInActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): MiniFaceCollection? {
            val data = MiniFaceCollection()
            data.landscapeBitmap = bigBitmap // key
            data.mapMiniFaceBitmap = mapMiniFace  // value
            return if (resultCode == Activity.RESULT_OK) data else null
        }

    }

    inner class MiniFaceCollection {
        var result: Int = Activity.RESULT_OK
        var landscapeBitmap: ArrayList<String>? = ArrayList()
        var mapMiniFaceBitmap: Map<String, ArrayList<String>>? = mutableMapOf()
    }

}
