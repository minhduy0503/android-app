package com.dev.fitface.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dev.fitface.R
import com.dev.fitface.api.models.report.Session
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants

class SessionAdapter(
    private val mContext: Context,
    var reportData: ArrayList<Session>?,
    val actionToParent: CallToAction?
) : RecyclerView.Adapter<SessionAdapter.SessionHolder>() {

    inner class SessionHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val timeLayout: ConstraintLayout? = itemView.findViewById(R.id.timeLayout)
        private val tvSessionDate: TextView? = itemView.findViewById(R.id.tvSessionDate)
        private val tvCampusName: TextView? = itemView.findViewById(R.id.tvCampusName)
        private val tvClassroomName: TextView? = itemView.findViewById(R.id.tvClassroomName)
        private val tvDuration: TextView? = itemView.findViewById(R.id.tvDuration)
        private val btnViewMore: View? = itemView.findViewById(R.id.btnEdit)

        fun bind(item: Session?) {
            item.let {
                if(AppUtils.isInProgress(it?.sessdate!!, (it.duration!!))){
                    timeLayout?.setBackgroundResource(R.drawable.bgr_custom_present)
                } else {
                    timeLayout?.setBackgroundResource(R.drawable.bgr_custom_past)
                }
                tvSessionDate?.text = AppUtils.getTime(it.sessdate!!)
                tvCampusName?.text = "Cơ sở: ${it.campus}"
                tvClassroomName?.text = "Phòng học: ${it.room}"
                tvDuration?.text = "Thời lượng: ${AppUtils.getDuration(it.duration!!)}"
                btnViewMore?.setOnClickListener(this)
            }
        }


        override fun onClick(v: View?) {
            val pos = adapterPosition
            val bundle = Bundle()
            bundle.putString(Constants.Param.dataType, Constants.Obj.session)
            bundle.putParcelable(Constants.Param.dataSelected, reportData?.get(pos))
            actionToParent?.action(bundle)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SessionHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.item_session_in_course, parent, false)
        return SessionHolder(view)
    }

    override fun onBindViewHolder(holder: SessionHolder, position: Int) {
        holder.bind(reportData?.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return reportData?.size ?: 0
    }

}