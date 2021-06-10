package com.dev.fitface.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.fitface.R
import com.dev.fitface.adapter.StudentAdapter
import com.dev.fitface.api.models.report.ReportCheckIn
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.viewmodel.CourseDetailActivityViewModel
import kotlinx.android.synthetic.main.fragment_student_in_course.*


class StudentInCourseFragment : Fragment() {

    interface OnStudentInCourseFragmentInteractionListener {
        fun onStudentInCourseInteraction(bundle: Bundle)
    }

    private var mListener: OnStudentInCourseFragmentInteractionListener? = null
    private var mContext: Context? = null
    private var mCallFromChild: CallToAction? = null


    private lateinit var mReportDetailByCourseIdSubscriber: Observer<List<ReportCheckIn>?>
    private var mCourseDetailActivityViewModel: CourseDetailActivityViewModel? =null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (context is OnStudentInCourseFragmentInteractionListener){
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnStudentInCourseFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroy() {
        unsubscribeLiveData()
        super.onDestroy()
    }

    private fun unsubscribeLiveData() {
        mCourseDetailActivityViewModel?.report?.removeObserver(mReportDetailByCourseIdSubscriber)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_in_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValue()
        initLiveData()
        intiListener()
    }

    private fun intiListener() {

    }

    private fun initLiveData() {
        activity?.let {
            mCourseDetailActivityViewModel = ViewModelProvider(it).get(CourseDetailActivityViewModel::class.java)
        }
        subscriberLiveData()
    }

    private fun subscriberLiveData() {
        // TODO: Something
        mReportDetailByCourseIdSubscriber = Observer { data ->
            data?.let {
                mContext?.let {
                    val studentAdapter = StudentAdapter(it, ArrayList(data), mCallFromChild)
                    rvStudent.layoutManager = LinearLayoutManager(it)
                    rvStudent.setHasFixedSize(true)
                    rvStudent.adapter = studentAdapter
                }
            }
        }

        mCourseDetailActivityViewModel?.report?.observe(viewLifecycleOwner, mReportDetailByCourseIdSubscriber)
    }

    private fun initValue() {
        mCallFromChild = object : CallToAction{
            override fun action(bundle: Bundle?) {
                bundle?.let {
                    mListener?.onStudentInCourseInteraction(bundle)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            StudentInCourseFragment().apply {
                arguments = Bundle()
            }
    }
}