package com.dev.fitface.view.fragments

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
import com.dev.fitface.utils.base64ToImage
import com.dev.fitface.viewmodel.AutoCheckInActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_check_in_result.*
import java.util.*

class CheckInResultFragment : BottomSheetDialogFragment(), View.OnClickListener {

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme

    interface OnResultDialogInteraction {
        fun onResultInteraction(bundle: Bundle?)
    }

    private lateinit var mFaceStrSubscriber: Observer<String?>
    private var mFaceViewModel: AutoCheckInActivityViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_in_result, container, false)
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
        val timer = object: CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.text = "${count --}"
            }

            override fun onFinish() {

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

            }

            R.id.btnRetry -> {

            }

            R.id.btnClose -> {
                dialog?.dismiss()
            }

            R.id.btnReport -> {

            }
        }
    }
}