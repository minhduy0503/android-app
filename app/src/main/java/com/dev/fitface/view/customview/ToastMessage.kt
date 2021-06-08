package com.dev.fitface.view.customview

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.dev.fitface.R

@Suppress("DEPRECATION")
class ToastMessage{

    enum class Type(val type: Int) {
        SUCCESS(1),
        ERROR(2)
    }

    companion object {

        const val SHORT = 200
        const val LONG = 1000

        fun makeText(context: Context?, message: String, duration: Int, type: Int): Toast {
            val toast = Toast(context)
            toast.setGravity(Gravity.FILL , 0, 0);
            toast.duration = duration
            val layout: View = LayoutInflater.from(context).inflate(R.layout.toast_message_layout, null, false)
            val l1 = layout.findViewById(R.id.tvMessage) as TextView
            val rootLayoutToast = layout.findViewById(R.id.rootLayoutToast) as ConstraintLayout
            val img: ImageView = layout.findViewById(R.id.imgType) as ImageView
            rootLayoutToast.setBackgroundColor(Color.argb(150, 0, 0, 0))
            l1.text = message

            when (type) {
                Type.SUCCESS.type -> {
                    img.setImageResource(R.drawable.ic_correct)
                }
                else -> {
                    img.setImageResource(R.drawable.ic_incorrect)
                }

            }

            toast.view = layout
            return toast
        }

    }

}