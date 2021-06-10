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
import com.dev.fitface.api.models.report.ReportCheckIn
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.utils.Constants
import com.google.android.material.imageview.ShapeableImageView

class StudentAdapter(
    private val mContext: Context,
    var reportData: ArrayList<ReportCheckIn>?,
    val actionToParent: CallToAction?
) : RecyclerView.Adapter<StudentAdapter.StudentHolder>() {

    inner class StudentHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val ctrRootView: ConstraintLayout? = itemView.findViewById(R.id.ctrRootView)
        private val imgStatusCheckInNow: View? = itemView.findViewById(R.id.imgStatusCheckInNow)
        private val imgAvatar: ShapeableImageView? = itemView.findViewById(R.id.avatarStudent)
        private val tvStudentId: TextView? = itemView.findViewById(R.id.tvStudentId)
        private val tvStudentName: TextView? = itemView.findViewById(R.id.tvStudentName)
        private val tvCheckedSessionNumber: TextView? =
            itemView.findViewById(R.id.tvCheckedSessionNumber)
        private val tvCheckedByHandNumber: TextView? =
            itemView.findViewById(R.id.tvCheckedByHandNumber)
        private val tvOvertimeSessionNumber: TextView? =
            itemView.findViewById(R.id.tvOvertimeSessionNumber)
        private val tvAbsentSessionNumber: TextView? =
            itemView.findViewById(R.id.tvAbsentSessionNumber)

        init {
            ctrRootView?.setOnClickListener(this)
        }

        fun bind(item: ReportCheckIn?) {
            item?.let {
                tvStudentId?.text = it.username
                tvStudentName?.text = it.name
                tvCheckedSessionNumber?.text = it.c.toString()
                tvCheckedByHandNumber?.text = it.b.toString()
                tvOvertimeSessionNumber?.text = it.t.toString()
                tvAbsentSessionNumber?.text = it.v.toString()
                when(it.presentStatusid){
                    1 -> {
                        imgStatusCheckInNow?.setBackgroundResource(R.color.green_kelly)
                    }
                    2 -> {
                        imgStatusCheckInNow?.setBackgroundResource(R.color.skin)

                    }
                    3 -> {
                        imgStatusCheckInNow?.setBackgroundResource(R.color.yellow)
                    }
                    4 -> {
                        imgStatusCheckInNow?.setBackgroundResource(R.color.red_error)
                    }
                    else -> {
                        imgStatusCheckInNow?.setBackgroundResource(R.color.grey)
                    }
                }
            }
        }

        override fun onClick(v: View?) {
            val pos = adapterPosition
            val bundle = Bundle()
            bundle.putString(Constants.Param.dataType, Constants.Obj.report)
            bundle.putParcelable(Constants.Param.dataSelected, reportData?.get(pos))
            actionToParent?.action(bundle)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.item_student_in_course, parent, false)
        return StudentHolder(view)
    }

    override fun onBindViewHolder(holder: StudentHolder, position: Int) {
        holder.bind(reportData?.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return reportData?.size ?: 0
    }

}