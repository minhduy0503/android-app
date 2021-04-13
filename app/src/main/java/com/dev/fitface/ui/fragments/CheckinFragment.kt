package com.dev.fitface.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.fitface.R
import com.dev.fitface.utils.BottomSheetType
import kotlinx.android.synthetic.main.fragment_checkin.*
import kotlinx.android.synthetic.main.fragment_checkin.view.*


class CheckinFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_checkin, container, false)
        view.btnStart.setOnClickListener {
            showBottomSheetDialog()
        }
        return view
    }
    private fun showBottomSheetDialog(){
        val bottomSheetFragment: BottomSheetFragment = BottomSheetFragment(BottomSheetType.CAMPUS, this)
        bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
    }

    companion object {
        @JvmStatic
        fun newInstance(): CheckinFragment = CheckinFragment()
    }
}