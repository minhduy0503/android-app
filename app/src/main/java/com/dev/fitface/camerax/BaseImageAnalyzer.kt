package com.dev.fitface.camerax

import android.annotation.SuppressLint
import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage

abstract class BaseImageAnalyzer<T> : ImageAnalysis.Analyzer {

    abstract val graphicOverlay: GraphicOverlay

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        mediaImage?.let { image ->
            detectInImage(InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees))
                    .addOnSuccessListener { results ->
                        onSuccess(
                                results,
                                graphicOverlay,
                                image,
                        )
                    }
                    .addOnFailureListener { e ->
                        graphicOverlay.clear()
                        graphicOverlay.postInvalidate()
                        onFailure(e)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
        }
    }

    abstract fun stop()

    protected abstract fun detectInImage(image: InputImage): Task<T>

    protected abstract fun onSuccess(
            results: T,
            graphicOverlay: GraphicOverlay,
            image: Image
    )

    protected abstract fun onFailure(e: Exception)
}