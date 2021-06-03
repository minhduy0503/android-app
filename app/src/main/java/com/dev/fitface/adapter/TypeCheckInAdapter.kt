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
import com.dev.fitface.data.CheckInTypeData
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.utils.Constants

/**
 * Created by Dang Minh Duy on 16,May,2021
 */
class TypeCheckInAdapter(private val context: Context, var typeData: ArrayList<CheckInTypeData>?, val actionToParent: CallToAction?): RecyclerView.Adapter<TypeCheckInAdapter.TypeCheckInHolder>(){

    inner class TypeCheckInHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val ctrRootView: ConstraintLayout?
        private val tvOption: TextView?

        init {
            ctrRootView = itemView.findViewById(R.id.ctrRootView) as ConstraintLayout
            tvOption = itemView.findViewById(R.id.tvOption) as TextView
            ctrRootView.setOnClickListener(this)
        }

        fun bind(item: CheckInTypeData?){
            item?.let { typeData ->
                typeData.name?.let {
                    tvOption?.text = it
                }
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            typeData?.forEach {
                it.isSelected = 0
            }
            typeData?.get(position)?.isSelected = 1
            notifyDataSetChanged()
            val bundle = Bundle()
            bundle.putString(Constants.Param.dataType,Constants.Obj.typeCheckIn)
            bundle.putParcelable(Constants.Param.dataSelected, typeData?.get(position))
            actionToParent?.action(bundle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeCheckInHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bottomsheet, parent, false)
        return TypeCheckInHolder(view)
    }

    override fun onBindViewHolder(holder: TypeCheckInHolder, position: Int) {
        holder.bind(typeData?.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return typeData?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}