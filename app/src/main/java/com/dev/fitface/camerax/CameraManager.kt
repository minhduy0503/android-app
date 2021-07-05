package com.dev.fitface.camerax

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
import com.dev.fitface.mlkit.FaceContourDetectionProcessor
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.EyeStatus
import com.dev.fitface.utils.FaceSize
import com.dev.fitface.utils.SharedPrefs
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraManager(
    private val context: Context,
    private val finderView: PreviewView,
    private val lifecycleOwner: LifecycleOwner,
    private val graphicOverlay: GraphicOverlay,
    private val mCameraCallback: CameraCallback?,
    private val checkInMode: Int
) {

    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    lateinit var imageAnalyzer: ImageAnalysis
    lateinit var cameraExecutor: ExecutorService
    lateinit var imageCapture: ImageCapture

    private var mCallback: FaceResultCallback? = null
    lateinit var metrics: DisplayMetrics
    var rotation: Float = 0f
    private var cameraSelectorOption =
        if (checkInMode == Constants.CameraMode.automatic) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK

    private var width: Int? = null
    private var height: Int? = null


    init {
        createNewExecutor()
        initCamera()
    }

    private fun initCamera() {
        when (checkInMode) {
            Constants.CameraMode.automatic -> {
                graphicOverlay.cameraSelector = CameraSelector.LENS_FACING_FRONT
                mCallback = object : FaceResultCallback {
                    override fun onFaceSize(size: FaceSize) {
                        mCameraCallback?.onFaceSizeNotify(size)
                    }

                    override fun onNotFrontFace() {
                        mCameraCallback?.onFrontFaceNotify()
                    }

                    override fun onFaceLocated(faceRect: Rect) {
                        mCameraCallback?.onFaceCapture(faceRect)
                    }

                    override fun onFaceLocated(faceRect: ArrayList<Rect>?) {
                    }

                    override fun onFaceOutside() {
                        mCameraCallback?.onFaceOutsideNotify()
                    }

                    override fun onNumberOfFace() {
                        mCameraCallback?.onFaceInvalidNumber()
                    }

                    override fun onNoFace() {
                        mCameraCallback?.onFaceNone()
                    }

                    override fun onEye(eyeStatus: EyeStatus) {
                        mCameraCallback?.onEyeNotify(eyeStatus)
                    }
                }
            }

            Constants.CameraMode.manual -> {
                graphicOverlay.cameraSelector = CameraSelector.LENS_FACING_BACK
                mCallback = object : FaceResultCallback {
                    override fun onFaceSize(size: FaceSize) {
                    }

                    override fun onNotFrontFace() {
                    }

                    override fun onFaceLocated(faceRect: Rect) {

                    }

                    override fun onFaceLocated(faceRect: ArrayList<Rect>?) {
                        mCameraCallback?.onFaceCapture(faceRect)
                    }

                    override fun onFaceOutside() {
                    }

                    override fun onNumberOfFace() {
                    }

                    override fun onNoFace() {
                    }

                    override fun onEye(eyeStatus: EyeStatus) {
                    }
                }
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

    private fun setUpPinchToZoom() {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio: Float = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1F
                val delta = detector.scaleFactor
                camera?.cameraControl?.setZoomRatio(currentZoomRatio * delta)
                return true
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

                width = metrics.widthPixels
                height = metrics.heightPixels
                SharedPrefs.instance.put(Constants.Param.width, width)
                SharedPrefs.instance.put(Constants.Param.height, height)


                imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setTargetResolution(Size(metrics.widthPixels, metrics.heightPixels))
                    .build()
                    .also {
                        it.setAnalyzer(
                            cameraExecutor,
                            FaceContourDetectionProcessor(graphicOverlay, mCallback, checkInMode)
                        )
                    }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraSelectorOption)
                    .build()


                imageCapture =
                    ImageCapture.Builder()
                        .setTargetResolution(Size(metrics.widthPixels, metrics.heightPixels))
                        .build()

                if (checkInMode == Constants.CameraMode.manual) {
                    setUpPinchToZoom()
                }
                setCameraConfig(cameraProvider, cameraSelector)

            }, ContextCompat.getMainExecutor(context)
        )
    }

    fun changeCameraSelector() {
        cameraProvider?.unbindAll()
        cameraSelectorOption =
            if (cameraSelectorOption == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
        graphicOverlay.toggleSelector()
        startCamera()
    }

    fun isHorizontalMode(): Boolean {
        return rotation == 90f || rotation == 270f
    }

    fun isFrontMode(): Boolean {
        return cameraSelectorOption == CameraSelector.LENS_FACING_FRONT
    }

    fun stopCamera() {
        cameraProvider?.unbind(imageAnalyzer)
    }

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        private const val TAG = "CameraXBasic"
    }

}