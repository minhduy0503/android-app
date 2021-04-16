package com.dev.fitface.camerax

import android.annotation.SuppressLint
import android.graphics.Rect
import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage

abstract class BaseImageAnalyzer<T> : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        mediaImage?.let { image ->
            detectInImage(InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees))
                    .addOnSuccessListener { results ->
                        onSuccess(
                                image,
                                results,
                                image.cropRect,
                        )
                    }
                    .addOnFailureListener { e ->
//                        graphicOverlay.postInvalidate()
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
            image: Image?,
            results: T,
            rect: Rect,
    )

    protected abstract fun onFailure(e: Exception)
}