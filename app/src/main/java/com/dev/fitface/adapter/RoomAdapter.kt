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
import com.dev.fitface.api.models.`object`.Room

class RoomAdapter(private val context: Context, var roomData: ArrayList<Room>?, val actionToParent: CallToAction?): RecyclerView.Adapter<RoomAdapter.RoomHolder>(){

    inner class RoomHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val ctrRootView: ConstraintLayout?
        private val tvOption: TextView?
        private val imgIsChosen: ImageView?

        init {
            ctrRootView = itemView.findViewById(R.id.ctrRootView) as ConstraintLayout
            tvOption = itemView.findViewById(R.id.tvOption) as TextView
            imgIsChosen = itemView.findViewById(R.id.imgOptionCheck) as ImageView
            ctrRootView.setOnClickListener(this)
        }

        fun bind(item: Room?){
            item?.let { room ->
                if(room.isSelected == 1){
                    imgIsChosen?.visibility = View.VISIBLE
                } else {
                    imgIsChosen?.visibility = View.INVISIBLE
                }
                room.name?.let {
                    tvOption?.text = it
                }
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            roomData?.forEach {
                it.isSelected = 0
            }
            roomData?.get(position)?.isSelected = 1
            notifyDataSetChanged()
            val bundle = Bundle()
            bundle.putString("type","Room")
            bundle.putParcelable("selectedRoom", roomData?.get(position))
            actionToParent?.action(bundle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bottomsheet, parent, false)
        return RoomHolder(view)
    }

    override fun onBindViewHolder(holder: RoomHolder, position: Int) {
        holder.bind(roomData?.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return roomData?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}