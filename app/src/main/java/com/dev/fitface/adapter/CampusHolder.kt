package com.dev.fitface.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.fitface.R
import kotlinx.android.synthetic.main.item_campus.view.*

class CampusHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
    var tvCampus: TextView = itemView!!.findViewById(R.id.tvOption)
}