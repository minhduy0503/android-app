package com.dev.fitface.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dev.fitface.R
import com.dev.fitface.api.models.face.MiniFace

import com.dev.fitface.utils.base64ToImage
import com.google.android.material.imageview.ShapeableImageView

class FaceCaptureAdapter(
    private val mContext: Context,
    var dataSrc: ArrayList<MiniFace>?,
) : RecyclerView.Adapter<FaceCaptureAdapter.FaceCaptureHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FaceCaptureAdapter.FaceCaptureHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.item_capture_face_student, parent, false)
        return FaceCaptureHolder(view)
    }

    override fun onBindViewHolder(holder: FaceCaptureAdapter.FaceCaptureHolder, position: Int) {
        holder.bind(dataSrc?.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return dataSrc?.size ?: 0
    }

    inner class FaceCaptureHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val imgCaptured: ShapeableImageView? = itemView.findViewById(R.id.imgCaptured)

        fun bind(item: MiniFace?) {
            item?.let {
                imgCaptured?.setImageBitmap(it.bm.base64ToImage())
            }
        }

       /* override fun onClick(v: View?) {
            val bundle = Bundle()
            val position = adapterPosition
            val item = dataSrc?.get(position)
            when (item?.isSelected) {
                false -> {
                    item.isSelected = true
                    bundle.putString(Constants.Param.action, Constants.CallAction.add)
                    bundle.putString(Constants.Param.image, item.bm)
                    notifyDataSetChanged()
                }
                true -> {
                    item.isSelected = false
                    bundle.putString(Constants.Param.action, Constants.CallAction.del)
                    bundle.putString(Constants.Param.image, item.bm)
                    notifyDataSetChanged()
                }

            }
            notifyDataSetChanged()
            actionToParent?.action(bundle)
        }*/
    }

}