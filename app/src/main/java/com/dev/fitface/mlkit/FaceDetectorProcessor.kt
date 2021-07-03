package com.dev.fitface.mlkit

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import com.dev.fitface.camerax.BaseImageAnalyzer
import com.dev.fitface.camerax.GraphicOverlay
import com.dev.fitface.interfaces.FaceResultCallback
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.EyeStatus
import com.dev.fitface.utils.FaceSize
import com.dev.fitface.utils.SharedPrefs
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.IOException
import kotlin.math.min
import kotlin.math.pow


class FaceContourDetectionProcessor(
    private val view: GraphicOverlay,
    private val callback: FaceResultCallback?,
    private val checkInMode: Int?
) : BaseImageAnalyzer<List<Face>>() {

    private val automaticCheckInOption = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    private val manualCheckInOption = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
        .build()

    private val widthScreen = SharedPrefs.instance[Constants.Param.width, Int::class.java]
    private val heightScreen = SharedPrefs.instance[Constants.Param.height, Int::class.java]
    private val centerX = widthScreen?.div(2) ?: 0
    private val centerY = heightScreen?.div(2) ?: 0

    // Radius of frame
    private val radiusOfFrame = (min(centerX, centerY)) * 0.65F

    // Radius of minimum frame & area of Rect
    private val minFrameRadius = radiusOfFrame * 0.5F
    private val minFrameArea = (2 * minFrameRadius).toDouble().pow(2.0)

    // Radius of maximum frame & area of Rect
    private val maxFrameRadius = radiusOfFrame * 1.2F
    private val maxFrameArea = (2 * maxFrameRadius).toDouble().pow(2.0)


    // Init Rect contain the frame of detection
    private val frameRect = Rect().apply {
        left = (centerX - maxFrameRadius).toInt()
        top = (centerY - maxFrameRadius).toInt()
        right = (centerX + maxFrameRadius).toInt()
        bottom = (centerY + maxFrameRadius).toInt()
    }

    /*   // Init min Rect to limit the range of detection
     private val minRect = Rect().apply {
         left = (centerX - minFrameRadius).toInt()
         top = (centerY - minFrameRadius).toInt()
         right = (centerX + minFrameRadius).toInt()
         bottom = (centerY + minFrameRadius).toInt()
     }*/

    private val detector =
        if (checkInMode == Constants.CameraMode.automatic) FaceDetection.getClient(
            automaticCheckInOption
        ) else FaceDetection.getClient(manualCheckInOption)

    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: $e")
        }
    }

    override fun onSuccess(
        results: List<Face>,
        graphicOverlay: GraphicOverlay,
        rect: Rect
    ) {
        graphicOverlay.clear()
        results.forEach { face ->
            val faceGraphic = FaceContourGraphic(graphicOverlay, face, rect)
            graphicOverlay.add(faceGraphic)

            Log.i(
                "Debug",
                "${face.boundingBox.left} - ${face.boundingBox.top} - ${face.boundingBox.right} - ${face.boundingBox.bottom} "
            )

            if (checkInMode == Constants.CameraMode.automatic) {
                if (results.size > 1) {
                    callback?.onNumberOfFace()
                    return@forEach
                }

                val faceBoundingBox = face.boundingBox

                // Check face is outside or inside
                if (faceOnFrame(faceBoundingBox)) {
                    // If face is in detection frame, check other trigger:
                    // Check front face:
                    if (isFrontFace(face)) {
                        // Check distance between face and device:
                        if (isValidFaceSize(face) == FaceSize.VALID) {
                            // Check eye:
                            if (isValidEye(face) == EyeStatus.VALID) {
                                // Capture face
                                callback?.onFaceLocated(faceBoundingBox)
                                return@forEach
                            } else {
                                callback?.onEye(isValidEye(face))
                                return@forEach
                            }
                        } else {
                            callback?.onFaceSize(isValidFaceSize(face))
                            return@forEach
                        }
                    } else {
                        callback?.onNotFrontFace()
                        return@forEach
                    }
                } else {
                    // send message outside
                    callback?.onFaceOutside()
                    return@forEach
                }
            }

        }
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {
        Log.w(TAG, "Face Detector failed.$e")
    }

    private fun translateCoordinate(faceRect: Rect): Rect {
        return Rect().apply {
            left = widthScreen!! - faceRect.right
            top = faceRect.top
            right = widthScreen - faceRect.left
            bottom = faceRect.bottom
        }
    }

    private fun faceOnFrame(faceRect: Rect): Boolean {

        // Calculate coordinate of face bounding box:
        val realFaceRect = translateCoordinate(faceRect)
        Log.i("Debug","-- real: ${realFaceRect.left} ${realFaceRect.top} ${realFaceRect.right} ${realFaceRect.bottom} ")
        // Check: frameRect contains realFaceRect ?
        return frameRect.contains(realFaceRect)
    }

    private fun isFrontFace(face: Face): Boolean {
        val faceDegree = face.headEulerAngleY
        if (faceDegree >= -12 && faceDegree <= 12)
            return true
        return false
    }

    private fun isValidBrightness(bitmap: Bitmap?): Int {
        if (bitmap == null) {
            return 0
        } else {
            var R: Int = 0
            var G: Int = 0
            var B: Int = 0
            val height = bitmap.height
            val width = bitmap.width
            var n: Int = 0
            var pixels: IntArray = IntArray(width * height)
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
            pixels.forEach { pixel ->
                R += Color.red(pixel)
                G += Color.green(pixel)
                B += Color.blue(pixel)
                n++
            }
            return (R + G + B) / (n * 3)
        }
    }

    // Config face size
    private fun isValidFaceSize(face: Face): FaceSize {
        val areaOfFace = face.boundingBox.width() * face.boundingBox.height()
        return if (areaOfFace < minFrameArea)
            FaceSize.SMALL
        else if (areaOfFace > minFrameArea && areaOfFace < maxFrameArea)
            FaceSize.VALID
        else
            FaceSize.BIG
    }

    private fun isValidEye(face: Face): EyeStatus {
        val leftOpenProbability: Float = face.leftEyeOpenProbability ?: -1.0F
        val rightOpenProbability: Float = face.rightEyeOpenProbability ?: -1.0F

        return if (leftOpenProbability < EYE_OPEN_PROBABILITY && rightOpenProbability >= EYE_OPEN_PROBABILITY)
            EyeStatus.LEFT_EYE_CLOSE
        else if (leftOpenProbability >= EYE_OPEN_PROBABILITY && rightOpenProbability < EYE_OPEN_PROBABILITY)
            EyeStatus.RIGHT_EYE_CLOSE
        else if (leftOpenProbability < EYE_OPEN_PROBABILITY && rightOpenProbability < EYE_OPEN_PROBABILITY)
            EyeStatus.ALL_EYES_CLOSE
        else
            EyeStatus.VALID
    }


    companion object {
        private const val TAG = "FaceDetectorProcessor"
        private const val EYE_OPEN_PROBABILITY = 0.4F
    }

}