package com.dev.fitface.mlkit

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import androidx.camera.core.ImageProxy
import com.dev.fitface.camerax.BaseImageAnalyzer
import com.dev.fitface.camerax.GraphicOverlay
import com.dev.fitface.interfaces.FaceDetectorDelegate
import com.dev.fitface.interfaces.UIDelegate
import com.dev.fitface.utils.DETECT_STATUS
import com.dev.fitface.utils.calculateBrightnessEstimate
import com.dev.fitface.utils.imageToBitmap
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions


class FaceDetectorProcessor(private val view: GraphicOverlay) :
    BaseImageAnalyzer<List<Face>>(), UIDelegate{

    private var leftOfView: Float = 0F
    private var topOfView: Float = 0F
    private var rightOfView: Float = 0F
    private var bottomOfView: Float = 0F
    private var isProcessing: Boolean = false

    private var faceDetectorDelegate: FaceDetectorDelegate? = null

    private val realtimeOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()

    private val detector = FaceDetection.getClient(realtimeOpts)

    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun stop() {
        try {
            detector.close()
        } catch (e: Exception){
            Log.e(TAG, "Exception thrown while trying to close Face Detector: $e")
        }
    }

    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun onSuccess(imageProxy: ImageProxy, results: List<Face>, graphicOverlay: GraphicOverlay, rect: Rect) {
        graphicOverlay.clear()
        results.forEach { face ->
            val faceGraphic = FaceContourGraphic(graphicOverlay, face, rect)
            graphicOverlay.add(faceGraphic)

            /**
             * Xử lí các trường hợp của face khi detect *
             * */
            // Convert imageProxy into Bitmap:
            val bitmap = imageProxy.image?.imageToBitmap()

            // Brightness process:
            val brightnessLevel = bitmap?.calculateBrightnessEstimate(bitmap)
            if (brightnessLevel!! < lowBrightnessThreshold){
                isProcessing = true
                faceDetectorDelegate?.onProcess(DETECT_STATUS.LOW_BRIGHTNESS)
                return
            }

            if (brightnessLevel > highBrightnessThreshold){
                isProcessing = true
                faceDetectorDelegate?.onProcess(DETECT_STATUS.HIGH_BRIGHTNESS)
                return
            }

            // Check: Is front face:
            if (face.headEulerAngleY < 12 || face.headEulerAngleY > 12){
                isProcessing = true
                faceDetectorDelegate?.onProcess(DETECT_STATUS.NOT_FRONT_FACE)
                return
            }

            // Check: Is eyes opened:
            if(!((face.leftEyeOpenProbability > LEFT_EYE_OPEN_PROBABILITY) && (face.rightEyeOpenProbability > RIGHT_EYE_OPEN_PROBABILITY))){
                isProcessing = true
                faceDetectorDelegate?.onProcess(DETECT_STATUS.EYE)
                return
            }

            // Check: Face is located in the frame
            val leftF = (face.boundingBox.left) * 1F
            val topF = (face.boundingBox.top) * 1F
            val rightF = (face.boundingBox.right) * 1F
            val bottomF = (face.boundingBox.bottom) * 1F

            if(!isInside(leftF, topF, rightF, bottomF, leftOfView, topOfView, rightOfView, bottomOfView)){
                isProcessing = true
                faceDetectorDelegate?.onProcess(DETECT_STATUS.OUTSIDE)
                return
            } else {
                isProcessing = false
                val faceWidth = face.boundingBox.width()
                val faceHeight = face.boundingBox.height()
                faceDetectorDelegate?.onCapture(isProcessing, leftF.toInt(), topF.toInt(), faceWidth, faceHeight)
            }

        }
        graphicOverlay.postInvalidate()

    }

    override fun onFailure(e: Exception) {

    }

    private fun isInside(leftF: Float, topF: Float, rightF: Float, bottomF: Float,
                    leftV: Float, topV: Float, rightV: Float, bottomV: Float): Boolean{
        if (topF< topV) return false
        if (bottomF > bottomV) return false
        if (leftF < leftV) return false
        return rightF <= rightV
    }


    companion object {
        private const val lowBrightnessThreshold = 80
        private const val highBrightnessThreshold = 220

        private const val LEFT_EYE_OPEN_PROBABILITY = 0.4
        private const val RIGHT_EYE_OPEN_PROBABILITY = 0.4

        private const val TAG = "FaceDetectorProcessor"
    }

    override fun onViewListener(left: Float, top: Float, right: Float, bottom: Float) {
        leftOfView = left
        topOfView = top
        rightOfView = right
        bottomOfView = bottom
    }
}