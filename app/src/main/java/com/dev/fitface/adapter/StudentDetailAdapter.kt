package com.dev.fitface.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dev.fitface.R
import com.dev.fitface.api.models.report.CheckInInfo
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants

class StudentDetailAdapter(
    private val mContext: Context,
    var reportData: ArrayList<CheckInInfo>?,
    val actionToParent: CallToAction?
) : RecyclerView.Adapter<StudentDetailAdapter.StudentDetailHolder>() {

    inner class StudentDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val tvTimeSession: TextView? = itemView.findViewById(R.id.tvTimeSession)
        private val tvCampusName: TextView? = itemView.findViewById(R.id.tvCampusName)
        private val tvClassroomName: TextView? = itemView.findViewById(R.id.tvClassroomName)
        private val tvTimeIn: TextView? = itemView.findViewById(R.id.tvTimeIn)
        private val tvCheckInStatus: TextView? = itemView.findViewById(R.id.tvCheckInStatus)
        private val imgCheckInStatus: ImageView? = itemView.findViewById(R.id.imgStatusCheckIn)
        private val timeLayout: ConstraintLayout? = itemView.findViewById(R.id.timeLayout)
        private val btnEdit: ImageView? = itemView.findViewById(R.id.btnEdit)

        fun bind(item: CheckInInfo?) {
            item?.let {
                tvTimeSession?.text = AppUtils.getTime(it.sessdate!!)
                tvCampusName?.text = "Cơ sở: ${it.campus}"
                tvClassroomName?.text = "Phòng học: ${it.room}"
                tvTimeIn?.text = AppUtils.getHourAndMinute(it.timein)
                when (it.statusid) {
                    1, 2 -> {
                        tvCheckInStatus?.text = "Đã điểm danh"
                        tvCheckInStatus?.setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.green_kelly
                            )
                        )
                        imgCheckInStatus?.setImageResource(R.drawable.ic_checked)
                    }
                    3 -> {
                        tvCheckInStatus?.text = "Điểm danh trễ"
                        tvCheckInStatus?.setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.yellow
                            )
                        )
                        imgCheckInStatus?.setImageResource(R.drawable.ic_overtime)
                    }
                    4 -> {
                        tvCheckInStatus?.text = "Vắng học"
                        tvCheckInStatus?.setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.red_error
                            )
                        )
                        imgCheckInStatus?.setImageResource(R.drawable.ic_absent)
                    }
                    else -> {
                        tvCheckInStatus?.text = "N/A"
                        tvCheckInStatus?.setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.black
                            )
                        )
                        imgCheckInStatus?.setImageResource(R.drawable.ic_absent)
                    }
                }
                if (AppUtils.isInProgressBetweenTimes(it?.sessdate!!, AppUtils.getCurrentTime())) {
                    timeLayout?.setBackgroundResource(R.drawable.bgr_custom_present)
                } else {
                    timeLayout?.setBackgroundResource(R.drawable.bgr_custom_past)
                }
            }
            btnEdit?.setOnClickListener(this)
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
    ): StudentDetailAdapter.StudentDetailHolder {
        val view =
            LayoutInflater.from(mContext)
                .inflate(R.layout.item_student_in_session_detail, parent, false)
        return StudentDetailHolder(view)
    }

    override fun onBindViewHolder(holder: StudentDetailHolder, position: Int) {
        holder.bind(reportData?.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return reportData?.size ?: 0
    }
}