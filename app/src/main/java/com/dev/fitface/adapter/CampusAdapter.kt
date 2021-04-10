package com.dev.fitface.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.fitface.R
import com.dev.fitface.models.Campus

class CampusAdapter(private var campus: ArrayList<Campus>): RecyclerView.Adapter<CampusHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampusHolder {
        return CampusHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_campus, parent, false))
    }

    override fun onBindViewHolder(holder: CampusHolder, position: Int) {
        holder.tvCampus.text = campus[position].toString()
    }

    override fun getItemCount(): Int {
        return campus.size
    }
}