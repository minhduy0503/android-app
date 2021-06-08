package com.dev.fitface.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.fitface.R


class StudentInCourseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_in_course, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            StudentInCourseFragment().apply {
                arguments = Bundle()
            }
    }
}