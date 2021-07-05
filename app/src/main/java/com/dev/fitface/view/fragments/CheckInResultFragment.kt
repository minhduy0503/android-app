package com.dev.fitface.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dev.fitface.R
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.face.CheckInResponse
import com.dev.fitface.api.models.face.Face
import com.dev.fitface.api.models.face.FaceResponse
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
    private lateinit var mFaceStrSubscribe: Observer<String?>
    private lateinit var mFindFaceSubscribe: Observer<Resource<FaceResponse>>
    private var mFaceViewModel: AutoCheckInActivityViewModel? = null
    private lateinit var timer: CountDownTimer
    private var responseFindFace: Face? = null

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
        mFaceViewModel?.faceStr?.removeObserver(mFaceStrSubscribe)
        mFaceViewModel?.findFaceResponse?.removeObserver(mFindFaceSubscribe)
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
        mFaceStrSubscribe = Observer { base64 ->
            base64?.let {
                val bitmap = it.base64ToImage()
                imgCheckIn.setImageBitmap(bitmap)
            }
        }
        mFaceViewModel?.faceStr?.observe(viewLifecycleOwner, mFaceStrSubscribe)

        mFindFaceSubscribe = Observer { res ->
            if (res?.resource?.data?.get(0)?.status == 200) {
                res.resource?.data?.let {
                    responseFindFace = it[0]
                    tvStudentId.text = it[0].username
                    spacer.text = " - "
                    tvStudentName.text = "${it[0].firstname} ${it[0].lastname}" ?: ""
                }
            }
        }
        mFaceViewModel?.findFaceResponse?.observe(viewLifecycleOwner, mFindFaceSubscribe)

    }

    fun setResultCheckInMessage(msg: String, statusCode: Int) {
        when (statusCode) {
            200 -> {
                if (msg.contains("registered", ignoreCase = true)) {
                    tvCheckInResult.text = "Tìm thấy thông tin sinh viên"
                    tvCheckInResult.setTextColor(resources.getColor(R.color.field_green))
                } else {
                    tvCheckInResult.text = "Điểm danh thành công"
                    tvCheckInResult.setTextColor(resources.getColor(R.color.field_green))
                }
            }

            400 -> {
                when {
                    msg.contains("already checked", ignoreCase = true) -> {
                        tvCheckInResult.text = "Bạn đã điểm danh trước đó"
                        tvCheckInResult.setTextColor(resources.getColor(R.color.yellow))
                    }
                    msg.contains("create the log", ignoreCase = true) -> {
                        tvCheckInResult.text = "Không thể ghi nhận kết quả điểm danh"
                        tvCheckInResult.setTextColor(resources.getColor(R.color.red))
                    }
                    else -> {
                        tvCheckInResult.text = "Thông tin điểm danh không đầy đủ"
                        tvCheckInResult.setTextColor(resources.getColor(R.color.yellow))
                    }
                }
            }

            404 -> {
                when {
                    msg.contains("not registered", ignoreCase = true) -> {
                        tvCheckInResult.text = "Bạn chưa đăng kí khuôn mặt"
                        tvCheckInResult.setTextColor(resources.getColor(R.color.yellow))
                    }
                    msg.contains("not any", ignoreCase = true) -> {
                        tvCheckInResult.text = "Không tồn tại buổi điểm danh"
                        tvCheckInResult.setTextColor(resources.getColor(R.color.yellow))
                    }
                    else -> {
                        tvCheckInResult.text = "Bạn không thuộc môn học này"
                        tvCheckInResult.setTextColor(resources.getColor(R.color.yellow))
                    }
                }

            }
        }
        closeDialogTimer(3)
    }

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
                bundle.putString(
                    Constants.Param.studentId,
                    responseFindFace?.username
                )
                mListener?.onCheckInResultFragmentInteraction(bundle)
            }
        }
        timer.start()
    }

    private fun initListener() {
        btnConfirm.setOnClickListener(this)
        btnRetry.setOnClickListener(this)
        btnReport.setOnClickListener(this)
    }

    fun closeDialog() {
        dialog?.dismiss()
    }

    private fun closeDialogTimer(time: Int) {
        timer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                val bundle = Bundle()
                bundle.putString(
                    Constants.ActivityName.autoCheckInActivity,
                    Constants.Param.startCamera
                )
                mListener?.onCheckInResultFragmentInteraction(bundle)
                dialog?.dismiss()
            }
        }
        timer.start()
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
                if (tvCheckInResult.text.isNullOrBlank()) {
                    btnConfirm.text = "Đồng ý"
                    val bundle = Bundle()
                    bundle.putString(
                        Constants.ActivityName.autoCheckInActivity,
                        Constants.Param.confirm
                    )
                    bundle.putString(
                        Constants.Param.studentId,
                        responseFindFace?.username
                    )
                    mListener?.onCheckInResultFragmentInteraction(bundle)
                } else {
                    val bundle = Bundle()
                    bundle.putString(
                        Constants.ActivityName.autoCheckInActivity,
                        Constants.Param.startCamera
                    )
                    mListener?.onCheckInResultFragmentInteraction(bundle)
                }
            }

            R.id.btnRetry -> {
                val bundle = Bundle()
                bundle.putString(
                    Constants.ActivityName.autoCheckInActivity,
                    Constants.Param.retry
                )
                mListener?.onCheckInResultFragmentInteraction(bundle)
                dialog?.dismiss()
            }

            R.id.btnReport -> {
                val bundle = Bundle()
                bundle.putString(Constants.ActivityName.autoCheckInActivity, Constants.Param.report)
                bundle.putString(Constants.Param.userTaken, responseFindFace?.username)
                mListener?.onCheckInResultFragmentInteraction(bundle)
                dialog?.dismiss()
            }
        }
    }
}