package com.dev.fitface.interfaces

import android.graphics.Rect
import com.dev.fitface.utils.EyeStatus
import com.dev.fitface.utils.FaceSize

/**
 * Created by Dang Minh Duy on 03,May,2021
 */
interface CameraCallback {
    fun onFaceCapture(rect: Rect)
    fun onFaceSizeNotify(size: FaceSize)
    fun onFrontFaceNotify()
    fun onFaceOutsideNotify()
    fun onFaceInvalidNumber()
    fun onFaceNone()
    fun onEyeNotify(status: EyeStatus)
}