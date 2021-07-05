package com.dev.fitface.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.dev.fitface.R
import com.dev.fitface.api.models.face.ManualCheckInResponse
import com.dev.fitface.api.models.face.MiniFaceCollection
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.utils.base64ToImage
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.item_result_manual_check_in.view.*


class ManualCheckInItemAdapter(
    private val mContext: Context,
    val dataSrc: ArrayList<MiniFaceCollection>?,
    val resultSrc: ArrayList<ManualCheckInResponse>?,
    val actionToParent: CallToAction?
) : RecyclerView.Adapter<ManualCheckInItemAdapter.ManualCheckInItemHolder>() {

    private val viewPool = RecycledViewPool()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManualCheckInItemHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.item_result_manual_check_in, parent, false)
        return ManualCheckInItemHolder(view)
    }

    override fun onBindViewHolder(holder: ManualCheckInItemHolder, position: Int) {
        holder.bind(dataSrc?.getOrNull(position), resultSrc?.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return dataSrc?.size ?: 0
    }

    inner class ManualCheckInItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgStudents: ShapeableImageView = itemView.findViewById(R.id.imgStudents)
        private val rvCapturedFace: RecyclerView = itemView.findViewById(R.id.rvCapturedFace)
        private val rvResultFace: RecyclerView = itemView.findViewById(R.id.rvResultFace)

        fun bind(itemCapture: MiniFaceCollection?, itemResult: ManualCheckInResponse?) {
            itemCapture?.let {
                imgStudents.setImageBitmap(it.bitmapLandmark?.base64ToImage())

                val captureFaceLayoutManager = LinearLayoutManager(
                    itemView.rvCapturedFace.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                captureFaceLayoutManager.initialPrefetchItemCount = it.miniFaceArray?.size ?: 0
                val faceCaptureAdapter = FaceCaptureAdapter(
                    rvCapturedFace.context,
                    ArrayList(it.miniFaceArray),
                    actionToParent
                )

                itemView.rvCapturedFace.layoutManager = captureFaceLayoutManager
                itemView.rvCapturedFace.adapter = faceCaptureAdapter
                itemView.rvCapturedFace.setRecycledViewPool(viewPool)
            }

            itemResult?.let {
                val resultFaceLayoutManager = LinearLayoutManager(
                    itemView.rvResultFace.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                resultFaceLayoutManager.initialPrefetchItemCount = it.data?.size ?: 0
                val faceRegisterAdapter = FaceRegisterAdapter(
                    rvResultFace.context,
                    ArrayList(it.data)
                )

                itemView.rvResultFace.layoutManager = resultFaceLayoutManager
                itemView.rvResultFace.adapter = faceRegisterAdapter
                itemView.rvResultFace.setRecycledViewPool(viewPool)
            }
        }
    }

}