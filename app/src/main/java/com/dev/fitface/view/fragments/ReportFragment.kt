package com.dev.fitface.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.dev.fitface.R
import com.dev.fitface.utils.Constants
import kotlinx.android.synthetic.main.fragment_report.*


class ReportFragment : DialogFragment(), View.OnClickListener {

    override fun getTheme(): Int = R.style.DialogCorners

    interface OnReportFragmentInteractionListener {
        fun onReportFragmentInteraction(bundle: Bundle)
    }

    private var mListener: OnReportFragmentInteractionListener? = null
    private var mistakenStudentId: String? = null
    private var newStudentId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnReportFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnReportFragmentInteractionListener")
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

    private fun initListener() {
        btnClose.setOnClickListener(this)
        btnConfirm.setOnClickListener(this)
        edtStudentId.setOnClickListener(this)
    }

    private fun initView() {
        tvStudentCheckInResult.text = mistakenStudentId
    }

    private fun initValue() {
        arguments?.let {
            mistakenStudentId = it.getString(Constants.Param.studentId, "")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): ReportFragment {
            val fragment = ReportFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnClose -> {
                val bundle = Bundle()
                bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.close)
                mListener?.onReportFragmentInteraction(bundle)
            }

            R.id.btnConfirm -> {
                val bundle = Bundle()
                newStudentId = edtStudentId.text.toString()
                bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.confirm)
                bundle.putString(Constants.Param.studentId, newStudentId)
                mListener?.onReportFragmentInteraction(bundle)
            }
        }
        dialog?.dismiss()
    }
}