package com.dev.fitface.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlin.math.min


class RoundFrameLayout(context: Context?, attrs: AttributeSet) :
        FrameLayout(context!!, attrs) {
    private val mRadius: Float = attrs.getAttributeFloatValue(null, "corner_radius", 0f)
    private val mPath = Path()
    val mRect = RectF()
        get() = field

    override fun onDraw(canvas: Canvas) {
        val savedState = canvas.save()
        val w = width.toFloat()
        val h = height.toFloat()
        mPath.reset()
        mRect[0f, 0f, w] = h
        mPath.addRoundRect(mRect, mRadius, mRadius, Path.Direction.CCW)
        mPath.close()
        super.onDraw(canvas)
        canvas.restoreToCount(savedState)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // compute the mPath
        val centerX = w / 2f // calculating half width
        val centerY = h / 3f // calculating half height
        mPath.reset()
        mPath.addCircle(centerX, centerY, (min(centerX, centerY) * 0.6).toFloat(), Path.Direction.CW)
//      mPath.addOval(centerX - 50, centerY - 100, centerX + 50, centerY + 100, Path.Direction.CCW)
        mPath.close()
    }

    override fun dispatchDraw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(mPath)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }
}
