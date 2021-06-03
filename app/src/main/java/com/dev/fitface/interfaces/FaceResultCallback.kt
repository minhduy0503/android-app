package com.dev.fitface.interfaces

import android.graphics.Rect
import com.dev.fitface.utils.EyeStatus
import com.dev.fitface.utils.FaceSize

/**
 * Created by Dang Minh Duy on 04,May,2021
 */
interface FaceResultCallback {
    fun onFaceSize(size: FaceSize)
    fun onNotFrontFace()
    fun onFaceLocated(faceRect: Rect)
    fun onFaceOutside()
    fun onNumberOfFace()
    fun onNoFace()
    fun onEye(eyeStatus: EyeStatus)
}