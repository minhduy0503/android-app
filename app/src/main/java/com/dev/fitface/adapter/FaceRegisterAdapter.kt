package com.dev.fitface.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.dev.fitface.R
import com.dev.fitface.api.models.face.Face
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.utils.Constants
import com.google.android.material.imageview.ShapeableImageView

class FaceRegisterAdapter(
    private val mContext: Context,
    val dataSrc: ArrayList<Face>?
) : RecyclerView.Adapter<FaceRegisterAdapter.FaceRegisterHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaceRegisterHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.item_registered_face, parent, false)
        return FaceRegisterHolder(view)
    }

    override fun onBindViewHolder(holder: FaceRegisterHolder, position: Int) {
        holder.bind(dataSrc?.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return dataSrc?.size ?: 0
    }


    inner class FaceRegisterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgRegistered: ShapeableImageView? = itemView.findViewById(R.id.imgRegistered)
        private val tvStudentId: TextView? = itemView.findViewById(R.id.tvStudentId)

        fun bind(item: Face?) {
            val circularProgressDrawable = CircularProgressDrawable(mContext)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(Color.WHITE)
            circularProgressDrawable.start()

            item?.let {
                imgRegistered?.let {
                    if (item.status == 200) {
                        Glide
                            .with(imgRegistered)
                            .load(item.userpictureurl)
                            .centerCrop()
                            .placeholder(circularProgressDrawable)
                            .error(R.drawable.ic_user_placeholder)
                            .into(imgRegistered)
                    } else {
                        Glide
                            .with(imgRegistered)
                            .load(Constants.Obj.errorAvatar)
                            .centerCrop()
                            .placeholder(circularProgressDrawable)
                            .error(R.drawable.ic_user_placeholder)
                            .into(imgRegistered)
                    }

                }
                tvStudentId?.text = item.username
            }
        }
    }
}