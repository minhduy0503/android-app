package com.dev.fitface.view.customview

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
    private val mRect = RectF()

    var radius: Float = 0F
    var centerX: Float = 0F
    var centerY: Float = 0F


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
        centerX = w / 2f // calculating half width
        centerY = h / 2f // calculating half height
        radius = (min(centerX, centerY) * 0.65F)

        mPath.reset()
        mPath.addCircle(centerX, centerY, radius, Path.Direction.CW)
        mPath.close()
    }

    override fun dispatchDraw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(mPath)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }
}
