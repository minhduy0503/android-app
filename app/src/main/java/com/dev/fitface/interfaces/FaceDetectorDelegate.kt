package com.dev.fitface.interfaces

import com.dev.fitface.utils.DETECT_STATUS

interface FaceDetectorDelegate {
    fun onCapture(isProcessing: Boolean, left: Int, top: Int, right: Int, bottom: Int)
    fun onProcess(status: DETECT_STATUS)
}