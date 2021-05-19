package com.dev.fitface.camerax

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.ScaleGestureDetector
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.dev.fitface.interfaces.CameraCallback
import com.dev.fitface.interfaces.FaceResultCallback
import com.dev.fitface.mlkit.FaceDetectorProcessor
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.EyeStatus
import com.dev.fitface.utils.FaceSize
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
        private val context: Context,
        private val finderView: PreviewView,
        private val lifecycleOwner: LifecycleOwner,
        private val graphicOverlay: GraphicOverlay,
        private val mActivityResultCallback: CameraCallback?,
        private val type: Int
) {

    companion object {
        private const val TAG = "CameraManager"
    }

    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var mCallback: FaceResultCallback? = null

    lateinit var cameraExecutor: ExecutorService
    lateinit var imageCapture: ImageCapture
    lateinit var metrics: DisplayMetrics


    var rotation: Float = 0f
    private var cameraSelectorOption = if (type == Constants.CameraMode.automatic) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK


    init {
        createNewExecutor()
        when(type){
            Constants.CameraMode.automatic -> {
                initFrontCamera()
            }
            Constants.CameraMode.manual -> {
                initBackCamera()
            }
        }


    }

    private fun initBackCamera() {

    }

    private fun initFrontCamera() {
        mCallback = object : FaceResultCallback {
            override fun onFaceSize(size: FaceSize) {
                mActivityResultCallback?.onFaceSizeNotify(size)
            }

            override fun onNotFrontFace() {
                mActivityResultCallback?.onFrontFaceNotify()
            }

            override fun onFaceLocated(faceRect: Rect) {
                mActivityResultCallback?.onFaceCapture(faceRect)
            }

            override fun onFaceOutside() {
                mActivityResultCallback?.onFaceOutsideNotify()
            }

            override fun onNumberOfFace() {
                mActivityResultCallback?.onFaceInvalidNumber()
            }

            override fun onNoFace() {
                mActivityResultCallback?.onFaceNone()
            }

            override fun onEye(eyeStatus: EyeStatus) {
                mActivityResultCallback?.onEyeNotify(eyeStatus)
            }

        }
    }

    private fun createNewExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun setCameraConfig(
            cameraProvider: ProcessCameraProvider?,
            cameraSelector: CameraSelector
    ) {
        try {
            cameraProvider?.unbindAll()
            camera = cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalyzer
            )
            preview?.setSurfaceProvider(
                    finderView.createSurfaceProvider()
            )
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpPinchToZoom() {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio: Float = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1F
                val delta = detector.scaleFactor
                camera?.cameraControl?.setZoomRatio(currentZoomRatio * delta)
                return true//        fragment.getRoomData(data?.id!!)

            }
        }
        val scaleGestureDetector = ScaleGestureDetector(context, listener)
        finderView.setOnTouchListener { _, event ->
            finderView.post {
                scaleGestureDetector.onTouchEvent(event)
            }
            return@setOnTouchListener true
        }
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
                Runnable {
                    cameraProvider = cameraProviderFuture.get()
                    preview = Preview.Builder().build()
                    metrics = DisplayMetrics().also { finderView.display.getRealMetrics(it) }

                    imageAnalyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .setTargetResolution(Size(metrics.widthPixels, metrics.heightPixels))
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor, FaceDetectorProcessor(graphicOverlay, mCallback))
                            }

                    val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(cameraSelectorOption)
                            .build()

                    imageCapture =
                            ImageCapture.Builder()
                                    .setTargetResolution(Size(metrics.widthPixels, metrics.heightPixels))
                                    .build()

                    if (Constants.CameraMode.manual == 0){
                        setUpPinchToZoom()
                    }
                    setCameraConfig(cameraProvider, cameraSelector)

                         /* Do something better than
                    SharedPrefs.instance.put("widthScreen", metrics.widthPixels)
                    SharedPrefs.instance.put("heightScreen", metrics.heightPixels)*/

                }, ContextCompat.getMainExecutor(context)
        )
    }

    fun stopCamera(){
        cameraProvider?.unbindAll()
    }

    fun isHorizontalMode(): Boolean {
        return rotation == 90f || rotation == 270f
    }

    fun isFrontMode(): Boolean {
        return cameraSelectorOption == CameraSelector.LENS_FACING_FRONT
    }

}