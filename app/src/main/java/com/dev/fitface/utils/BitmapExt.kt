package com.dev.fitface.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.view.View


fun Bitmap.rotateFlipImage(degree: Float, isFrontMode: Boolean): Bitmap? {
    val realRotation = when (degree) {
        0f -> 90f
        90f -> 0f
        180f -> 270f
        else -> 180f
    }
    val matrix = Matrix().apply {
        if (isFrontMode) {
            preScale(-1.0f, 1.0f)
        }
        postRotate(realRotation)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

fun Bitmap.scaleImage(view: View, isHorizontalRotation: Boolean): Bitmap? {
    val ratio = view.width.toFloat() / view.height.toFloat()
    val newHeight = (view.width * ratio).toInt()

    return when (isHorizontalRotation) {
        true -> Bitmap.createScaledBitmap(this, view.width, newHeight, false)
        false -> Bitmap.createScaledBitmap(this, view.width, view.height, false)
    }
}

fun Bitmap.getBaseYByView(view: View, isHorizontalRotation: Boolean): Float {
    return when (isHorizontalRotation) {
        true -> (view.height.toFloat() / 2) - (this.height.toFloat() / 2)
        false -> 0f
    }
}

fun Bitmap.calculateBrightnessEstimate(bitmap: Bitmap): Int{
    var r: Int = 0
    var g: Int = 0
    var b: Int = 0

    val height: Int = bitmap.height
    val width: Int = bitmap.width
    var n: Int = 0
    val pixels:IntArray = IntArray(width * height)

    bitmap.getPixels(pixels, 0, 0, 0, 0, width, height)
    for (i in pixels){
        r+= Color.red(i)
        g+= Color.green(i)
        b+= Color.blue(i)
        n++
    }
    return (r + g + b) / (n * 3)
}
