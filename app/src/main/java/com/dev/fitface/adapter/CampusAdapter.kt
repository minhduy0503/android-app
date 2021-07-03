package com.dev.fitface.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dev.fitface.R
import com.dev.fitface.api.models.campus.Campus
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.utils.Constants


class CampusAdapter(val context: Context, var campusData: ArrayList<Campus>?, val actionToParent: CallToAction?): RecyclerView.Adapter<CampusAdapter.CampusHolder>(){

    inner class CampusHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val ctrRootView: ConstraintLayout?
        private val tvOption: TextView?

        init {
            ctrRootView = itemView.findViewById(R.id.ctrRootView) as ConstraintLayout
            tvOption = itemView.findViewById(R.id.tvOption) as TextView
            ctrRootView.setOnClickListener(this)
        }

        fun bind(item: Campus?){
            item?.let { campus ->
                campus.id?.let {
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
            bundle.putString(Constants.Param.dataType,Constants.Obj.campus)
            bundle.putParcelable(Constants.Param.dataSelected, campusData?.get(position))
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
