package com.dev.fitface.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.fitface.R
import com.dev.fitface.adapter.FaceCaptureAdapter
import com.dev.fitface.adapter.FaceRegisterAdapter
import com.dev.fitface.adapter.TypeCheckInAdapter
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.face.Face
import com.dev.fitface.api.models.face.FaceResponse
import com.dev.fitface.api.models.face.MiniFace
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.base64ToImage
import com.dev.fitface.view.customview.ToastMessage
import com.dev.fitface.viewmodel.ManualCheckInActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_check_in_result.*
import kotlinx.android.synthetic.main.fragment_manual_check_in_result.*
import kotlinx.android.synthetic.main.fragment_manual_check_in_result.btnConfirm

class ManualCheckInResultFragment : BottomSheetDialogFragment(),
    View.OnClickListener {

    interface OnManualCheckInFragmentInteractionListener {
        fun onManualCheckInFragmentInteraction(bundle: Bundle?)
    }

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme

    private var mListener: OnManualCheckInFragmentInteractionListener? = null

    // ViewModel
    private var mManualCheckInViewModel: ManualCheckInActivityViewModel? = null
    private lateinit var mCaptureFace: Observer<List<MiniFace>?>
    private lateinit var mFindFaceResponse: Observer<Resource<FaceResponse>>

    private var checkInSelectedStudent: ArrayList<String>? = arrayListOf()
    private var captureFace: ArrayList<MiniFace>? = null
    private var registeredFace: ArrayList<Face>? = null
    private var mCallFromChild: CallToAction? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual_check_in_result, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnManualCheckInFragmentInteractionListener)
            mListener = context
        else
            throw RuntimeException("$context must implement OnManualCheckInFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeLiveData()
    }

    private fun unsubscribeLiveData() {
        mManualCheckInViewModel?.capturedFace?.removeObserver(mCaptureFace)
        mManualCheckInViewModel?.findFaceResponse?.removeObserver(mFindFaceResponse)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValue()
        initLiveData()
        initListener()
    }

    private fun initValue() {
        checkInSelectedStudent?.clear()
        // Check In API:
        mCallFromChild = object : CallToAction {
            override fun action(bundle: Bundle?) {
                bundle?.let {
                    when (it.getString(Constants.Param.action)) {
                        Constants.CallAction.del -> {
                            val username = it.getString(Constants.Param.username)
                            checkInSelectedStudent?.remove(username!!)
                        }
                        Constants.CallAction.add -> {
                            val username = it.getString(Constants.Param.username)
                            checkInSelectedStudent?.add(username!!)
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun initListener() {
        btnConfirm.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
    }

    private fun initLiveData() {
        activity?.let {
            mManualCheckInViewModel =
                ViewModelProvider(it).get(ManualCheckInActivityViewModel::class.java)
        }
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        mCaptureFace = Observer { collection ->
            collection?.let {
                captureFace = ArrayList(it)
                val faceCapturedAdapter =
                    FaceCaptureAdapter(requireContext(), ArrayList(it))
                rvCapturedFace.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                rvCapturedFace.setHasFixedSize(true)
                rvCapturedFace.adapter = faceCapturedAdapter
            }
        }
        mManualCheckInViewModel?.capturedFace?.observe(viewLifecycleOwner, mCaptureFace)

        mFindFaceResponse = Observer { res ->
            res?.let { resource ->
                resource.resource?.data?.let { res ->
                    registeredFace = ArrayList(res)
                    val faceRegisteredAdapter =
                        FaceRegisterAdapter(requireContext(), ArrayList(res), mCallFromChild)
                    rvResultFace.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    rvResultFace.setHasFixedSize(true)
                    rvResultFace.adapter = faceRegisteredAdapter
                }
            }
        }
        mManualCheckInViewModel?.findFaceResponse?.observe(viewLifecycleOwner, mFindFaceResponse)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            ManualCheckInResultFragment().apply {
                arguments = bundle
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnConfirm -> {
                if (checkInSelectedStudent?.size == 0) {
                    ToastMessage.makeText(
                        requireContext(),
                        "Vui lòng chọn sinh viên để thực hiên điểm danh",
                        ToastMessage.SHORT,
                        ToastMessage.Type.ERROR.type
                    ).show()
                } else {
                    mManualCheckInViewModel?.checkInImage?.postValue(checkInSelectedStudent)
                }
            }
            R.id.btnCancel -> {
                tvCheckInResultStatus.visibility = View.INVISIBLE
                dialog?.dismiss()
            }
        }
    }

    fun updateUICheckInResult(dataCode: Int?, message: String?) {
        tvCheckInResultStatus.visibility = View.VISIBLE
        when (dataCode) {
            200 -> {
                tvCheckInResultStatus?.text = "Điểm danh thành công"
                tvCheckInResultStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green_kelly
                    )
                )
            }
            else -> {
                tvCheckInResultStatus?.text = "Đã có lỗi xảy ra, vui lòng thử lại"
                tvCheckInResultStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red_error
                    )
                )
            }
        }
    }
}