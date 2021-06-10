package com.dev.fitface.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.fitface.R
import com.dev.fitface.adapter.CourseAdapter
import com.dev.fitface.api.models.course.Course
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private var mListener: OnHomeFragmentInteractionListener? = null
    private var mContext: Context? = null
    private var mCallFromChild: CallToAction? = null


    private lateinit var mCoursesSubscriber: Observer<List<Course>?>
    private var mMainActivityViewModel: MainActivityViewModel? =null

    interface OnHomeFragmentInteractionListener {
        fun onHomeFragmentInteraction(bundle: Bundle)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (context is OnHomeFragmentInteractionListener)
            mListener = context
        else
            throw RuntimeException("$context must implement OnHomeFragmentInteractionListener")
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
        mMainActivityViewModel?.courses?.removeObserver(mCoursesSubscriber)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValue()
        initLiveData()
        intiListener()
    }

    private fun initValue() {
        mCallFromChild = object : CallToAction{
            override fun action(bundle: Bundle?) {
                bundle?.let {
                    mListener?.onHomeFragmentInteraction(it)
                }
            }

        }
    }


    private fun intiListener() {

    }


    private fun initLiveData() {
        activity?.let {
            mMainActivityViewModel = ViewModelProvider(it).get(MainActivityViewModel::class.java)
        }
        subscriberLiveData()
    }

    private fun subscriberLiveData() {
        mCoursesSubscriber = Observer { data ->
            data?.let {
                mContext?.let { context ->
                    val courseAdapter = CourseAdapter(context, ArrayList(data), mCallFromChild )
                    rvCourses.layoutManager = GridLayoutManager(context, 2)
                    rvCourses.setHasFixedSize(true)
                    rvCourses.adapter = courseAdapter
                }
            }
        }

        mMainActivityViewModel?.courses?.observe(viewLifecycleOwner, mCoursesSubscriber)
    }


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?): HomeFragment {
            val fragment = HomeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}