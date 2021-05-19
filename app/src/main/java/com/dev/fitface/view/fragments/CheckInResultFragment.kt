package com.dev.fitface.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.dev.fitface.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CheckInResultFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_in_result, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) = CheckInResultFragment()
    }
}