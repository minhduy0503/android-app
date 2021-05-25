package com.dev.fitface.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.fitface.R
import com.dev.fitface.adapter.CampusAdapter
import com.dev.fitface.adapter.RoomAdapter
import com.dev.fitface.adapter.TypeCheckInAdapter
import com.dev.fitface.api.models.campus.Campus
import com.dev.fitface.api.models.room.Room
import com.dev.fitface.data.CheckInTypeData
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*
import java.lang.RuntimeException


class BottomSheetSelectionFragment : BottomSheetDialogFragment(), View.OnClickListener {

    interface OnOptionBottomSheetInteractionListener {
        fun onSelectedInteraction(bundle: Bundle?)
    }

    private var mDialogType: String? = null
    private var mCallFromChild: CallToAction? = null
    private var mListener: OnOptionBottomSheetInteractionListener? = null

    private lateinit var dataCampus: ArrayList<Campus>
    private lateinit var dataRoom: ArrayList<Room>
    private lateinit var dataType: ArrayList<CheckInTypeData>

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOptionBottomSheetInteractionListener)
            mListener = context
        else
            throw RuntimeException("$context must implement OnOptionBottomSheetInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValue()
        initAdapter()
        initListener()
    }

    private fun initListener() {
        btnClose.setOnClickListener(this)
    }

    private fun initAdapter() {
        when (mDialogType) {
            Constants.Obj.campus -> {
                tvTitleBottomSheet.text = resources.getString(R.string.campus_selection)
                var campusAdapter = CampusAdapter(context!!, dataCampus, mCallFromChild)
                rvOption.layoutManager = LinearLayoutManager(context)
                rvOption.setHasFixedSize(true)
                rvOption.adapter = campusAdapter
            }
            Constants.Obj.room -> {
                tvTitleBottomSheet.text = resources.getString(R.string.room_selection)
                var roomAdapter = RoomAdapter(context!!, dataRoom, mCallFromChild)
                rvOption.layoutManager = LinearLayoutManager(context)
                rvOption.setHasFixedSize(true)
                rvOption.adapter = roomAdapter

            }
            Constants.Obj.typeCheckIn -> {
                tvTitleBottomSheet.text = resources.getString(R.string.type_selection)
                var typeAdapter = TypeCheckInAdapter(context!!, dataType, mCallFromChild)
                rvOption.layoutManager = LinearLayoutManager(context)
                rvOption.setHasFixedSize(true)
                rvOption.adapter = typeAdapter

            }
        }
    }


    private fun initValue() {
        arguments?.let { bundle ->
            mDialogType = bundle.getString(Constants.Param.typeBottomSheet)
            when (mDialogType) {
                Constants.Obj.campus -> {
                    dataCampus = bundle.getParcelableArrayList(Constants.Param.dataSrc)!!
                }
                Constants.Obj.room -> {
                    dataRoom = bundle.getParcelableArrayList(Constants.Param.dataSrc)!!
                }
                Constants.Obj.typeCheckIn -> {
                    dataType = bundle.getParcelableArrayList(Constants.Param.dataSrc)!!
                }
            }
        }

        mCallFromChild = object : CallToAction {
            override fun action(bundle: Bundle?) {
                mListener?.onSelectedInteraction(bundle)
                dialog?.dismiss()
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            btnClose?.id -> {
                dialog?.dismiss()
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?): BottomSheetSelectionFragment {
            val fragment = BottomSheetSelectionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}