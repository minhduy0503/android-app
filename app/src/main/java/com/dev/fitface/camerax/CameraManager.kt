package com.dev.fitface.camerax

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.dev.fitface.interfaces.FaceResultCallback
import com.dev.fitface.mlkit.FaceDetectorProcessor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
        private val context: Context,
        private val finderView: PreviewView,
        private val lifecycleOwner: LifecycleOwner,
        private val graphicOverlay: GraphicOverlay,
){

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
    var cameraSelectorOption = CameraSelector.LENS_FACING_FRONT


    init {
        createNewExecutor()
        mCallback = object: FaceResultCallback{
            override fun onFaceLocated(faceRect: Rect) {
                Log.i("Debug", "OK")
            }

            override fun onFaceOutside() {
            }

        }
    }

    private fun createNewExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    /**
     * Select checking and camera selection
     * */
/*    private fun selectAnalyzer(): ImageAnalysis.Analyzer {
        return when (analyzerVisionType) {
            VisionType.Object -> ObjectDetectionProcessor(graphicOverlay)
            VisionType.OCR -> TextRecognitionProcessor(graphicOverlay)
            VisionType.Face -> FaceContourDetectionProcessor(graphicOverlay)
            VisionType.Barcode -> BarcodeScannerProcessor(graphicOverlay)
        }
    }*/

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

/*    private fun setUpPinchToZoom() {
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
    }*/

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
                Runnable {
                    cameraProvider = cameraProviderFuture.get()
                    preview = Preview.Builder().build()

                    metrics =  DisplayMetrics().also { finderView.display.getRealMetrics(it) }

                    imageAnalyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
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
                    setCameraConfig(cameraProvider, cameraSelector)
                }, ContextCompat.getMainExecutor(context)
        )
    }

   /* fun changeCameraSelector() {
        cameraProvider?.unbindAll()
        cameraSelectorOption =
                if (cameraSelectorOption == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
                else CameraSelector.LENS_FACING_BACK
        graphicOverlay.toggleSelector()
        startCamera()
    }*/

    fun isHorizontalMode() : Boolean {
        return rotation == 90f || rotation == 270f
    }

    fun isFrontMode() : Boolean {
        return cameraSelectorOption == CameraSelector.LENS_FACING_FRONT
    }


}