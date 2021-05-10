package com.dev.fitface.view

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.dev.fitface.R


@Suppress("DEPRECATION")
class CustomToast(context: Context) : Toast(context) {

    companion object {
        fun makeText(applicationContext: Context?, message: String, duration: Int, type: Int): Toast {
            val toast = Toast(applicationContext)
            toast.duration = duration
            val layout: View = LayoutInflater.from(applicationContext).inflate(R.layout.customtoast_layout, null, false)
            val l1 = layout.findViewById(R.id.toast_text) as TextView
            val linearLayout = layout.findViewById(R.id.toast_type) as LinearLayout
            val img: ImageView = layout.findViewById(R.id.toast_icon) as ImageView

            toast.setGravity(Gravity.BOTTOM, 0, 90)
            l1.text = message
            if (type == 1) {
                linearLayout.setBackgroundResource(R.drawable.success_shape)
                img.setImageResource(R.drawable.ic_check)
            } else if (type == 2) {
                linearLayout.setBackgroundResource(R.drawable.warning_shape)
                img.setImageResource(R.drawable.ic_warning)
            } else if (type == 3) {
                linearLayout.setBackgroundResource(R.drawable.error_shape)
                img.setImageResource(R.drawable.ic_error)
            } else if (type == 4) {
                linearLayout.setBackgroundResource(R.drawable.confusing_shape)
                img.setImageResource(R.drawable.ic_reload)
            }
            toast.view = layout
            return toast
        }

        var SUCCESS = 1
        var WARNING = 2
        var ERROR = 3
        var CONFUSING = 4
        var SHORT = 50
        var LONG = 100
    }

}