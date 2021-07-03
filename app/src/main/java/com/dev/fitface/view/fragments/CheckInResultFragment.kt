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
import kotlinx.android.synthetic.main.fragment_check_in_result.*

class CheckInResultFragment : BottomSheetDialogFragment(),
    View.OnClickListener {


    interface OnResultCheckInFragmentInteractionListener {
        fun onCheckInResultFragmentInteraction(bundle: Bundle?)
    }

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme
    private var mListener: OnResultCheckInFragmentInteractionListener? = null
    private lateinit var mFaceStrSubscriber: Observer<String?>
    private var mFaceViewModel: AutoCheckInActivityViewModel? = null
    private lateinit var timer: CountDownTimer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        countDownTimer(time = 5)
    }

    private fun initLiveData() {
        activity?.let {
            mFaceViewModel = ViewModelProvider(it).get(AutoCheckInActivityViewModel::class.java)
        }
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        mFaceStrSubscriber = Observer { base64 ->
            base64?.let {
                val bitmap = it.base64ToImage()
                imgCheckIn.setImageBitmap(bitmap)
            }
        }
    }

/*
    private fun setStudentInfo() {
        val stsData = response?.get(0)
        if (stsData?.username?.isNotBlank() == true) {
            grStudentInfo.visibility = View.VISIBLE
            tvStudentId.text = response?.get(0)?.username
            tvStudentName.text = "${response?.get(0)?.firstname} ${response?.get(0)?.lastname}"
            Glide.with(this)
                .load(stsData.userpictureurl)
                .centerCrop()
                .error(R.drawable.ic_user_avtar)
                .into(profileStudent)
        } else {
            grStudentInfo.visibility = View.INVISIBLE
        }
    }
*/

    /*private fun setResultCheckInMessage() {
        when (response?.get(0)?.status) {
            200 -> {
                tvCheckInResult.text = "Điểm danh thành công"
                tvCheckInResult.setTextColor(resources.getColor(R.color.field_green))
            }

            404 -> {
                tvCheckInResult.text = "Buổi học không tồn tại hoặc bạn không thuộc khóa học này"
                tvCheckInResult.setTextColor(resources.getColor(R.color.red_error))
            }

            400 -> {
                tvCheckInResult.text = "Bạn đã điểm danh trước đó"
                tvCheckInResult.setTextColor(resources.getColor(R.color.yellow))
            }
        }
    }*/

/*    private fun initValue() {
        arguments?.let { bundle ->
            response = bundle.getParcelableArrayList(Constants.Param.dataSrc)
        }
    }*/

    private fun countDownTimer(time: Int) {
        var count = time
        timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                btnConfirm.text = "${count--}"
            }

            override fun onFinish() {
                btnConfirm.text = "Đồng ý"
                val bundle = Bundle()
                bundle.putString(
                    Constants.ActivityName.autoCheckInActivity,
                    Constants.Param.confirm
                )
                mListener?.onCheckInResultFragmentInteraction(bundle)
                dialog?.dismiss()
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


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): CheckInResultFragment {
            val fragment = CheckInResultFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(v: View?) {
        timer.cancel()
        when (v?.id) {
            R.id.btnConfirm -> {
                val bundle = Bundle()
                bundle.putString(
                    Constants.ActivityName.autoCheckInActivity,
                    Constants.Param.confirm
                )
                mListener?.onCheckInResultFragmentInteraction(bundle)
            }

            R.id.btnRetry -> {

            }

            R.id.btnReport -> {
                val bundle = Bundle()
               /* bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.report)
                bundle.putString(Constants.Param.studentId, response?.get(0)?.username)
                mListener?.onCheckInResultFragmentInteraction(bundle)*/
            }

            R.id.btnClose -> {
                val bundle = Bundle()
                bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.close)
                mListener?.onCheckInResultFragmentInteraction(bundle)
            }
        }
        dialog?.dismiss()
    }
}