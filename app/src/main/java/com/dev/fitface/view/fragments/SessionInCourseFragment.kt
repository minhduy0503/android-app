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
import com.dev.fitface.adapter.SessionAdapter
import com.dev.fitface.api.models.report.Session
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.viewmodel.CourseDetailActivityViewModel
import kotlinx.android.synthetic.main.fragment_session_in_course.*

class SessionInCourseFragment : Fragment() {

    interface OnSessionInCourseFragmentInteractionListener {
        fun onSessionInCourseInteraction(bundle: Bundle)
    }

    private var mListener: OnSessionInCourseFragmentInteractionListener? = null
    private var mContext: Context? = null
    private var mCallFromChild: CallToAction? = null

    private lateinit var mSessionDetailByCourseIdSubscriber: Observer<List<Session>?>
    private var mCourseDetailActivityViewModel: CourseDetailActivityViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (context is OnSessionInCourseFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnSessionInCourseFragmentInteractionListener")
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
        mCourseDetailActivityViewModel?.session?.removeObserver(mSessionDetailByCourseIdSubscriber)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session_in_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValue()
        initLiveData()
        initListener()
    }

    private fun initLiveData() {
        activity?.let {
            mCourseDetailActivityViewModel = ViewModelProvider(it).get(CourseDetailActivityViewModel::class.java)
        }
        subscriberLiveData()
    }

    private fun subscriberLiveData() {
        mSessionDetailByCourseIdSubscriber = Observer { data ->
            data?.let {
                mContext?.let {
                    val sessionAdapter = SessionAdapter(it, ArrayList(data), mCallFromChild)
                    rvSession.layoutManager = LinearLayoutManager(it)
                    rvSession.setHasFixedSize(true)
                    rvSession.adapter = sessionAdapter
                }
            }
        }
        mCourseDetailActivityViewModel?.session?.observe(viewLifecycleOwner, mSessionDetailByCourseIdSubscriber)
    }

    private fun initListener() {

    }

    private fun initValue() {
        mCallFromChild = object : CallToAction {
            override fun action(bundle: Bundle?) {
                bundle?.let {
                    mListener?.onSessionInCourseInteraction((bundle))
                }
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            SessionInCourseFragment().apply {
                arguments = Bundle()
            }
    }
}