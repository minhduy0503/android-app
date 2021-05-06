package com.dev.fitface.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.fitface.R
import com.dev.fitface.adapter.CampusAdapter
import com.dev.fitface.adapter.RoomAdapter
import com.dev.fitface.interfaces.CallToAction
import com.dev.fitface.models.Campus
import com.dev.fitface.models.Room
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet.view.*


class BottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var mListener: OnOptionDialogFragmentInteractionListener? = null
    private var mCallFromChild: CallToAction? = null
    private var mContext: Context? = null

    private var mDialogType: String? = null
    private lateinit var dataCampus: ArrayList<Campus>
    private lateinit var dataRoom: ArrayList<Room>
    private var campusID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initValue()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOptionDialogFragmentInteractionListener) {
            mListener = context
        } else {
            throw ClassCastException("$context must implement OnOptionDialogFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    private fun initValue() {
        arguments?.let {
            mDialogType = it.getString("DialogType")
            when (mDialogType) {
                "Campus" -> {
                    dataCampus = it.getParcelableArrayList("Data")!!
                }
                "Room" -> {
                    dataRoom = it.getParcelableArrayList("Data")!!
                }
            }

        }

        mCallFromChild = object : CallToAction {
            override fun action(bundle: Bundle?) {
                when (bundle?.getString("type")) {
                    "Campus" -> {
                        mListener?.onOptionCampusDialogFragmentInteraction(bundle)
                    }
                    "Room" -> {
                        mListener?.onOptionRoomDialogFragmentInteraction(bundle)
                    }
                }
                dialog?.dismiss()
            }
        }

    }

    override fun getTheme(): Int = R.style.BottomSheetMenuTheme

    interface OnOptionDialogFragmentInteractionListener {
        fun onOptionCampusDialogFragmentInteraction(bundle: Bundle?)
        fun onOptionRoomDialogFragmentInteraction(bundle: Bundle?)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        initView(view)
        initListener(view)
        return view
    }


    private fun initView(view: View) {
        when (mDialogType) {
            view.resources.getString(R.string.campus_type) -> {
                view.tvTitleBottomSheet.text = view.resources.getString(R.string.campus_selection)
                var campusAdapter = CampusAdapter(context!!, dataCampus, mCallFromChild)
                view.rvOption.layoutManager = LinearLayoutManager(context)
                view.rvOption.setHasFixedSize(true)
                view.rvOption.adapter = campusAdapter
            }
            view.resources.getString(R.string.room_type) -> {
                view.tvTitleBottomSheet.text = view.resources.getString(R.string.room_selection)
                var roomAdapter = RoomAdapter(context!!, dataRoom, mCallFromChild)
                view.rvOption.layoutManager = LinearLayoutManager(context)
                view.rvOption.setHasFixedSize(true)
                view.rvOption.adapter = roomAdapter
            }
        }
    }


    private fun initListener(view: View) {
        view.btnClose.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            btnClose?.id -> {
                dialog?.dismiss()
            }
        }
    }

    companion object {
        const val TAG = "BottomSheetFragment"

        @JvmStatic
        fun newInstance(bundle: Bundle?): BottomSheetFragment {
            val fragment = BottomSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}