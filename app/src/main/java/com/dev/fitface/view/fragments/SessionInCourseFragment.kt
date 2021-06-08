package com.dev.fitface.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dev.fitface.R

class SessionInCourseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session_in_course, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            SessionInCourseFragment().apply {
                arguments = Bundle()
            }
    }
}