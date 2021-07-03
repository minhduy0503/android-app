package com.dev.fitface.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.fitface.R
import com.dev.fitface.adapter.StudentDetailAdapter
import com.dev.fitface.api.models.report.ReportCheckIn
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.utils.Constants
import kotlinx.android.synthetic.main.fragment_student_in_course_detail.*


class StudentInCourseDetailFragment : Fragment(),
    View.OnClickListener {

    interface OnStudentInCourseDetailFragmentInteractionListener {
        fun onStudentInCourseDetailInteraction(bundle: Bundle)
    }

    private var mListener: OnStudentInCourseDetailFragmentInteractionListener? = null
    private var mContext: Context? = null
    private var mCallFromChild: CallToAction? = null

    private var data: ReportCheckIn? = null
    private var username: String? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (context is OnStudentInCourseDetailFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnStudentInCourseDetailFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValue()
        initView()
        initListener()
    }

    private fun initView() {
        data?.let { res ->
            mContext?.let {
                username = res.username
                tvProfileStudentId.text = res.username
                tvProfileStudentName.text = res.name
                val studentDetailAdapter =
                    StudentDetailAdapter(it, ArrayList(res.reports), mCallFromChild)
                rvSessionOfStudent.layoutManager = LinearLayoutManager(it)
                rvSessionOfStudent.setHasFixedSize(true)
                rvSessionOfStudent.adapter = studentDetailAdapter
            }
        }
    }

    private fun initListener() {

    }


    private fun initValue() {
        arguments?.let {
            data = it.getParcelable(Constants.Param.dataSelected)
        }

        mCallFromChild = object : CallToAction {
            override fun action(bundle: Bundle?) {
                bundle?.putString(Constants.ActivityName.courseDetailActivity, Constants.Param.confirm)
                bundle?.putString(Constants.Param.studentId, username)
                bundle?.let { mListener?.onStudentInCourseDetailInteraction(it) }
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_in_course_detail, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            StudentInCourseDetailFragment().apply {
                arguments = bundle
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBack -> {
                val bundle = Bundle()
                bundle.putString(Constants.ActivityName.courseDetailActivity, Constants.Param.close)
                mListener?.onStudentInCourseDetailInteraction(bundle)
            }
            else -> {

            }
        }
    }

}