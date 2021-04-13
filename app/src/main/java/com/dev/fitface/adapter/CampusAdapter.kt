package com.dev.fitface.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dev.fitface.R
import com.dev.fitface.models.Campus
import com.dev.fitface.ui.fragments.CheckinFragment



class CampusAdapter(context: Context, campus: List<Campus>, fragment: CheckinFragment): RecyclerView.Adapter<CampusAdapter.CampusHolder>(){

    private var mInflater: LayoutInflater? = null
    private var mList: List<Campus>? = null
    private var mFragment: Fragment? = null

    init {
        mInflater = LayoutInflater.from(context)
        mList = campus
        mFragment = fragment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampusHolder {
        val view: View? = mInflater?.inflate(R.layout.item_bottomsheet, parent, false)
        return CampusHolder(view!!)
    }

    override fun onBindViewHolder(holder: CampusHolder, position: Int) {
        val itemSelected: Campus? = mList?.get(position)
        holder.tvOption.text = itemSelected?.name
        holder.itemView.setOnClickListener {
            mFragment?.view?.findViewById<TextView>(R.id.tvCampus)?.text = itemSelected?.name
        }

    }

    override fun getItemCount(): Int {
        return mList?.size!!
    }

    inner class CampusHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvOption: TextView = itemView.findViewById(R.id.tvOption)
    }

}