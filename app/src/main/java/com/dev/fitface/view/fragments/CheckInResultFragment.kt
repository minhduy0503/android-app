package com.dev.fitface.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.base64ToImage
import com.dev.fitface.viewmodel.AutoCheckInActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_check_in_result.*
import java.lang.RuntimeException
import java.util.*

class CheckInResultFragment : BottomSheetDialogFragment(), View.OnClickListener {

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme

    private var mListener: OnResultCheckInFragmentInteractionListener? = null
    private lateinit var timer: CountDownTimer
    private var base64Str: String? = null

    interface OnResultCheckInFragmentInteractionListener {
        fun onResultInteraction(bundle: Bundle?)
    }

    private lateinit var mFaceStrSubscriber: Observer<String?>
    private var mFaceViewModel: AutoCheckInActivityViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_in_result, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnResultCheckInFragmentInteractionListener)
            mListener = context
        else
            throw RuntimeException("$context must implement OnResultCheckInFragmentInteractionListener")
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
        mFaceViewModel?.faceStr?.removeObserver(mFaceStrSubscriber)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLiveData()
        initListener()
        countDownTimer()
    }

    private fun countDownTimer() {
        var count = 5
        timer = object: CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.text = "${--count}"
            }

            override fun onFinish() {
                // auto call api

            }
        }
        timer.start()
    }

    private fun initListener() {
        btnConfirm.setOnClickListener(this)
        btnRetry.setOnClickListener(this)
        btnReport.setOnClickListener(this)
        btnClose.setOnClickListener(this)
    }

    private fun initLiveData() {
        activity?.let {
            mFaceViewModel = ViewModelProvider(it).get(AutoCheckInActivityViewModel::class.java)
        }
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        mFaceStrSubscriber = Observer {
            it?.let {
                base64Str = it
                val bitmap = it.base64ToImage()
                profileStudent.setImageBitmap(bitmap)
            }
        }

        mFaceViewModel?.faceStr?.observe(viewLifecycleOwner, mFaceStrSubscriber)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) = CheckInResultFragment()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btnConfirm -> {
                val bundle = Bundle()
                bundle.putString(Constants.FragmentName.autoCheckInResultFragment, Constants.Param.confirm)
                bundle.putString(Constants.Obj.faceStr, base64Str)
                mListener?.onResultInteraction(bundle)
            }

            R.id.btnRetry -> {
                val bundle = Bundle()
                bundle.putString(Constants.FragmentName.autoCheckInResultFragment, Constants.Param.retry)
                mListener?.onResultInteraction(bundle)
            }

            R.id.btnReport -> {
                val bundle = Bundle()
                bundle.putString(Constants.FragmentName.autoCheckInResultFragment, Constants.Param.report)
                bundle.putString(Constants.Param.confirm, base64Str)
            }

            R.id.btnClose -> {
                val bundle = Bundle()
                bundle.putString(Constants.FragmentName.autoCheckInResultFragment, Constants.Param.close)
                tvTimer.text = "0"
                timer.cancel()
                dialog?.dismiss()
            }
        }
    }
}