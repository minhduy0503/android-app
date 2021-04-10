package com.dev.fitface.mlkit

import android.graphics.Rect
import android.util.Log
import com.dev.fitface.camerax.BaseImageAnalyzer
import com.dev.fitface.camerax.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectorProcessor(private val view: GraphicOverlay) :
    BaseImageAnalyzer<List<Face>>(){

    companion object {
        private const val TAG = "FaceDetectorProcessor"
    }

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

    override fun onSuccess(results: List<Face>, graphicOverlay: GraphicOverlay, rect: Rect) {
        TODO("Not yet implemented")
    }

    override fun onFailure(e: Exception) {
        Log.w(TAG, "Face Detector failed.$e")
    }
}