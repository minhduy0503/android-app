package com.dev.fitface.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.fitface.R
import com.dev.fitface.api.models.report.CheckInInfo
import com.dev.fitface.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_edit_check_in.*


class EditCheckInFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var sessionId: Int? = null
    private var username: String? = null
    private var checkStatus: Int? = 0
    private var timeIn: Long? = null
    private var timeOut: Long? = null

    interface OnEditCheckInFragmentInteractionListener {
        fun onEditCheckInFragmentInteraction(bundle: Bundle?)
    }

    private var mListener: OnEditCheckInFragmentInteractionListener? = null

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditCheckInFragmentInteractionListener)
            mListener = context
        else
            throw RuntimeException("$context must implement OnEditCheckInFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValue()
        initListener()
    }

    private fun initValue() {
        arguments?.let {
            val data: CheckInInfo? = it.getParcelable(Constants.Param.dataSelected)
            username = it.getString(Constants.Param.studentId)
            sessionId = data?.sessionid
            timeIn = data?.timein
            timeOut = data?.timeout
            Log.i("Debug", "${username} - ${sessionId}")
        }
    }

    private fun initListener() {
        btnClose.setOnClickListener(this)
        checkBoxPos.setOnClickListener(this)
        checkBoxOver.setOnClickListener(this)
        checkBoxAbsent.setOnClickListener(this)
        btnConfirm.setOnClickListener(this)
        if (!(checkBoxAbsent.isChecked && checkBoxOver.isChecked && checkBoxPos.isChecked)) {
            checkStatus = 0
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_check_in, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            EditCheckInFragment().apply {
                arguments = bundle
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.checkBoxPos -> {
                checkStatus = 1
                checkBoxOver.isChecked = false
                checkBoxAbsent.isChecked = false
            }
            R.id.checkBoxOver -> {
                checkStatus = 3
                checkBoxPos.isChecked = false
                checkBoxAbsent.isChecked = false
            }
            R.id.checkBoxAbsent -> {
                checkStatus = 4
                checkBoxOver.isChecked = false
                checkBoxPos.isChecked = false
            }
            R.id.btnConfirm -> {
                if (checkStatus != 0) {
                    val bundle = Bundle()
                    bundle.putString(
                        Constants.ActivityName.courseDetailActivity,
                        Constants.Param.confirm
                    )
                    bundle.putInt(Constants.Param.sessionId, sessionId ?: -1)
                    bundle.putLong(Constants.Param.timeIn, timeIn ?: -1)
                    bundle.putLong(Constants.Param.timeOut, timeOut ?: -1)
                    bundle.putString(Constants.Param.studentId, username)
                    bundle.putInt(Constants.Param.checkInStatus, checkStatus ?: -1)
                    mListener?.onEditCheckInFragmentInteraction(bundle)
                    dialog?.dismiss()
                }
                btnConfirm.isEnabled = false
            }
            R.id.btnClose -> {
                val bundle = Bundle()
                bundle.putString(Constants.ActivityName.courseDetailActivity, Constants.Param.close)
                mListener?.onEditCheckInFragmentInteraction(bundle)
                dialog?.dismiss()
            }

        }
    }
}