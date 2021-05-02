package com.dev.fitface.interfaces

import android.graphics.Rect

interface FaceResultCallback {
    fun onFaceLocated(faceRect: Rect)
    fun onFaceOutside()
}