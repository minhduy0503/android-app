package com.dev.fitface.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dev.fitface.R
import com.dev.fitface.api.models.face.Face
import com.dev.fitface.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_check_in_result.*

class CheckInResultFragment : BottomSheetDialogFragment(), View.OnClickListener {

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme

    private var mListener: OnResultCheckInFragmentInteractionListener? = null
    private lateinit var timer: CountDownTimer

    private var response: List<Face>? = null

    interface OnResultCheckInFragmentInteractionListener {
        fun onCheckInResultFragmentInteraction(bundle: Bundle?)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValue()
        initView()
        initListener()
        countDownTimer(time = 5)
    }

    private fun initView() {
        setResultCheckInMessage()
        setStudentInfo()
    }

    private fun setStudentInfo() {
        val stsData = response?.get(0)
        if (stsData?.username?.isNotBlank() == true) {
            grStudentInfo.visibility = View.VISIBLE
            tvStudentId.text = response?.get(0)?.username
            tvStudentName.text = "${response?.get(0)?.firstname} ${response?.get(0)?.lastname}"
            Glide.with(this)
                .load(stsData.userpictureurl)
                .centerCrop()
                .error(R.drawable.ic_user_placeholder)
                .into(profileStudent)
        } else {
            grStudentInfo.visibility = View.INVISIBLE
        }
    }

    private fun setResultCheckInMessage() {
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
    }

    private fun initValue() {
        arguments?.let { bundle ->
            response = bundle.getParcelableArrayList(Constants.Param.dataSrc)

        }
    }

    private fun countDownTimer(time: Int) {
        var count = time
        timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvConfirm.text = "${count --}"
            }

            override fun onFinish() {
                tvConfirm.text = "Đồng ý"
                val bundle = Bundle()
                bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.confirm)
                mListener?.onCheckInResultFragmentInteraction(bundle)
                dialog?.dismiss()
            }
        }
        timer.start()
    }

    private fun initListener() {
        btnConfirm.setOnClickListener(this)
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
                bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.confirm)
                mListener?.onCheckInResultFragmentInteraction(bundle)
            }

            R.id.btnReport -> {
                val bundle = Bundle()
                bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.report)
                bundle.putString(Constants.Param.studentId, response?.get(0)?.username)
                mListener?.onCheckInResultFragmentInteraction(bundle)
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