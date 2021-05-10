package com.dev.fitface.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dev.fitface.R
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.api.models.`object`.Campus


class CampusAdapter(private val context: Context, var campusData: ArrayList<Campus>?, val actionToParent: CallToAction?): RecyclerView.Adapter<CampusAdapter.CampusHolder>(){

    inner class CampusHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val ctrRootView: ConstraintLayout?
        private val tvOption: TextView?
        private val imgIsChosen: ImageView?

        init {
            ctrRootView = itemView.findViewById(R.id.ctrRootView) as ConstraintLayout
            tvOption = itemView.findViewById(R.id.tvOption) as TextView
            imgIsChosen = itemView.findViewById(R.id.imgOptionCheck) as ImageView
            ctrRootView.setOnClickListener(this)
        }

        fun bind(item: Campus?){
            item?.let { campus ->
                if(campus.isSelected == 1){
                    imgIsChosen?.visibility = View.VISIBLE
                } else {
                    imgIsChosen?.visibility = View.INVISIBLE
                }
                campus.name?.let {
                    tvOption?.text = it
                }
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            campusData?.forEach {
                it.isSelected = 0
            }
            campusData?.get(position)?.isSelected = 1
            notifyDataSetChanged()
            val bundle = Bundle()
            bundle.putString("type","Campus")
            bundle.putParcelable("selectedCampus", campusData?.get(position))
            actionToParent?.action(bundle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampusHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bottomsheet, parent, false)
        return CampusHolder(view)
    }

    override fun onBindViewHolder(holder: CampusHolder, position: Int) {
        holder.bind(campusData?.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return campusData?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
