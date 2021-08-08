package com.dev.fitface.adapter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
    val dataSrc: ArrayList<Face>?,
    val actionToParent: CallToAction?
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


    inner class FaceRegisterHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val imgRegistered: ShapeableImageView? = itemView.findViewById(R.id.imgRegistered)
        private val tvStudentId: TextView? = itemView.findViewById(R.id.tvStudentId)
        private val imgCheck: ImageView? = itemView.findViewById(R.id.imgCheck)
        private val ctrRootView: ConstraintLayout? = itemView.findViewById(R.id.ctrRootView)

        init {
            ctrRootView?.setOnClickListener(this)
        }

        fun bind(item: Face?) {
            val circularProgressDrawable = CircularProgressDrawable(mContext)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(Color.WHITE)
            circularProgressDrawable.start()

            item?.let {
                imgCheck?.visibility = if (item.isSelected) View.VISIBLE else View.INVISIBLE
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

/*                    if (item.statusCheckIn == 0){
                        it.setStrokeColorResource(R.color.red)
                        it.strokeWidth = 1.0F
                    }
                    if (item.statusCheckIn == 1){
                        it.setStrokeColorResource(R.color.green_kelly)
                        it.strokeWidth = 1.0F
                    }*/
                }
                tvStudentId?.text = item.username ?: "N/A"
            }
        }

        override fun onClick(v: View?) {
            val bundle = Bundle()
            val position = adapterPosition
            val item = dataSrc?.get(position)
            if (item?.status == 200) {
                when (item.isSelected) {
                    false -> {
                        item.isSelected = true
                        bundle.putString(Constants.Param.action, Constants.CallAction.add)
                        bundle.putString(Constants.Param.username, item?.username)
                        notifyDataSetChanged()
                    }
                    true -> {
                        item.isSelected = false
                        bundle.putString(Constants.Param.action, Constants.CallAction.del)
                        bundle.putString(Constants.Param.username, item?.username)
                        notifyDataSetChanged()
                    }
                }
            }
            notifyDataSetChanged()
            actionToParent?.action(bundle)
        }
    }
}