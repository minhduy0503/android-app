package com.dev.fitface.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.fitface.R
import com.dev.fitface.interfaces.CampusItemDelegate
import com.dev.fitface.models.Campus
import java.util.*
import kotlin.collections.ArrayList


class CampusAdapter(private var campus: ArrayList<Campus>): RecyclerView.Adapter<CampusAdapter.CampusHolder>(){

    private var mItemDelegate: CampusItemDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampusHolder {
        return CampusHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bottomsheet, parent, false))
    }

    override fun onBindViewHolder(holder: CampusHolder, position: Int) {
        holder.tvOption.text = campus[position].name
        holder.tvOption.setOnClickListener{
            mItemDelegate?.onItemClick(campus[position])
        }
    }

    override fun getItemCount(): Int {
        return campus.size
    }

    inner class CampusHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvOption: TextView = itemView.findViewById(R.id.tvOption)
    }
}