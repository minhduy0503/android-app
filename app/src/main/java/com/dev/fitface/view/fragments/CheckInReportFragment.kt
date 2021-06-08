package com.dev.fitface.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.base64ToImage
import com.dev.fitface.viewmodel.AutoCheckInActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_check_in_report.*


class CheckInReportFragment : BottomSheetDialogFragment(), View.OnClickListener {

    interface OnCheckInReportFragmentInteractionListener {
        fun onCheckInReportFragmentInteraction(bundle: Bundle)
    }

    private var mListener: OnCheckInReportFragmentInteractionListener? = null
    private lateinit var mFaceStrSubscriber: Observer<String?>
    private var mFaceViewModel: AutoCheckInActivityViewModel? = null
    private lateinit var timer: CountDownTimer

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_in_report, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCheckInReportFragmentInteractionListener)
            mListener = context
        else
            throw RuntimeException("$context must implement OnCheckInReportFragmentInteractionListener")
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
        initCountDownTimer()
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
                val bitmap = it.base64ToImage()
                profileStudent.setImageBitmap(bitmap)
            }
        }
        mFaceViewModel?.faceStr?.observe(viewLifecycleOwner, mFaceStrSubscriber)
    }

    private fun initListener() {
        btnClose.setOnClickListener(this)
        btnConfirm.setOnClickListener(this)
        btnRetry.setOnClickListener(this)
    }

    private fun initCountDownTimer() {
        var count = 5
        timer = object: CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvConfirm.text = "${count--}"
            }

            override fun onFinish() {
                // auto call api
                tvConfirm.text = "Đồng ý"
                val bundle = Bundle()
                bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.confirm )
                mListener?.onCheckInReportFragmentInteraction(bundle)
                dialog?.dismiss()
            }
        }
        timer.start()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): CheckInReportFragment {
            val fragment = CheckInReportFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(v: View?) {
        timer.cancel()
        when (v?.id) {
            R.id.btnClose -> {
                val bundle = Bundle()
                bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.close )
                mListener?.onCheckInReportFragmentInteraction(bundle)
                dialog?.dismiss()
            }

            R.id.btnConfirm -> {
                val bundle = Bundle()
                bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.confirm )
                mListener?.onCheckInReportFragmentInteraction(bundle)
                dialog?.dismiss()
            }

            R.id.btnRetry -> {
                val bundle = Bundle()
                bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.retry )
                mListener?.onCheckInReportFragmentInteraction(bundle)
                dialog?.dismiss()
            }
        }
    }
}