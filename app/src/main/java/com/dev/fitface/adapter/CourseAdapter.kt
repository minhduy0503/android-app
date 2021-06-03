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
import com.dev.fitface.api.models.course.Course
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.utils.Constants
import kotlinx.android.synthetic.main.item_course.view.*

/**
 * Created by Dang Minh Duy on 25,May,2021
 */
class CourseAdapter(private val mContext: Context, var coursesData: ArrayList<Course>?, val actionToParent: CallToAction?): RecyclerView.Adapter<CourseAdapter.CourseHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseAdapter.CourseHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_course, parent, false)
        return CourseHolder(view)
    }

    override fun onBindViewHolder(holder: CourseAdapter.CourseHolder, position: Int) {
        holder.bind(coursesData?.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return coursesData?.size ?: 0
    }

    inner class CourseHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val ctrRootView: ConstraintLayout? = itemView.findViewById(R.id.ctrRootView)
        private val tvFullName: TextView? = itemView.findViewById(R.id.tvFullCourseName)
        private val tvShortName: TextView? = itemView.findViewById(R.id.tvShortCourseName)

        init {
            ctrRootView?.setOnClickListener(this)
        }

        fun bind(item: Course?){
            item?.let {
                tvFullName?.text = it.fullname ?: ""
                tvShortName?.text = it.shortname ?: ""
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val bundle = Bundle()
            bundle.putString(Constants.Param.dataType, Constants.Obj.course)
            bundle.putParcelable(Constants.Param.dataSelected, coursesData?.get(position))
            actionToParent?.action(bundle)
        }

    }
}