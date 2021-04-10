package com.dev.fitface.camerax

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.dev.fitface.mlkit.FaceDetectorProcessor
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
        private val context: Context,
        private val finderView: PreviewView,
        private val lifecycleOwner: LifecycleOwner,
        private val graphicOverlay: GraphicOverlay
) {

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        private const val TAG = "CameraManager"
    }

    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageAnalyzer: ImageAnalysis? = null

    lateinit var cameraExecutor: ExecutorService
    lateinit var imageCapture: ImageCapture
    lateinit var metrics: DisplayMetrics

    var rotation: Float = 0f
    var cameraSelectorOption = CameraSelector.LENS_FACING_BACK

    init {
        createNewExecutor()
    }

    private fun createNewExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun initAnalyzer(): ImageAnalysis.Analyzer{
        TODO("Implement face detector processor here ????")
    }

    private fun setCameraConfig(
            cameraProvider: ProcessCameraProvider?,
            cameraSelector: CameraSelector
    ){
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
        } catch (e: Exception){
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
                Runnable {
                    cameraProvider = cameraProviderFuture.get()
                    preview = Preview.Builder().build()
                    metrics =  DisplayMetrics().also { finderView.display.getRealMetrics(it) }

                    imageAnalyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .setTargetResolution(Size(metrics.widthPixels, metrics.heightPixels))
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor, FaceDetectorProcessor(graphicOverlay))
                            }

                    val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(cameraSelectorOption)
                            .build()

                    imageCapture =
                            ImageCapture.Builder()
                                    .setTargetResolution(Size(metrics.widthPixels, metrics.heightPixels))
                                    .build()
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

    fun isHorizontalMode() : Boolean {
        return rotation == 90f || rotation == 270f
    }

    fun isFrontMode() : Boolean {
        return cameraSelectorOption == CameraSelector.LENS_FACING_FRONT
    }
}