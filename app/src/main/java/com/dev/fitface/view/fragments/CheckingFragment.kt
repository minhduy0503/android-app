package com.dev.fitface.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.api.models.campus.Campus
import com.dev.fitface.api.models.room.Room
import com.dev.fitface.data.CheckInTypeData
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants
import com.dev.fitface.view.CustomToast
import com.dev.fitface.view.activity.AutoCheckInActivity
import com.dev.fitface.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.fragment_checkin.*
import java.lang.RuntimeException


class CheckingFragment : Fragment(), View.OnClickListener {

    private var mListener: OnSelectionInteractionListener? = null

    private lateinit var mCampusSubscriber: Observer<List<Campus>?>
    private lateinit var mRoomSubscriber: Observer<List<Room>?>
    private var mMainActivityViewModel: MainActivityViewModel? = null

    private var isCallApi: Boolean = false
    private var idCampus: String? = null

    interface OnSelectionInteractionListener {
        fun onSelection(type: String, id: String?)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSelectionInteractionListener)
            mListener = context
        else
            throw RuntimeException("$context must implement OnSelectionInteractionListener")
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
        mMainActivityViewModel?.campus?.removeObserver(mCampusSubscriber)
        mMainActivityViewModel?.roomByCampus?.removeObserver(mRoomSubscriber)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // After View created -> Init others
        initLiveData()
        initListener()
    }

    private fun initLiveData() {
        activity?.let {
            mMainActivityViewModel = ViewModelProvider(it).get(MainActivityViewModel::class.java)
        }
        subscriberLiveData()
    }

    private fun subscriberLiveData() {
        mCampusSubscriber = Observer {
            it?.let {
                Log.i("Debug", "-- ${it}")
                if (isCallApi) {
                    val bundle = Bundle()
                    bundle.putString(Constants.Param.typeBottomSheet, Constants.Obj.campus)
                    bundle.putParcelableArrayList(Constants.Param.dataSrc, ArrayList(it))
                    val bottomSheetFrag = BottomSheetSelectionFragment.newInstance(bundle)
                    bottomSheetFrag.show(fragmentManager!!, Constants.FragmentName.bottomSheetFragment)
                    isCallApi = false
                }
            }
        }

        mMainActivityViewModel?.campus?.observe(viewLifecycleOwner, mCampusSubscriber)

        mRoomSubscriber = Observer {
            it?.let {
                Log.i("Debug", "-- ${it}")
                if (isCallApi) {
                    val bundle = Bundle()
                    bundle.putString(Constants.Param.typeBottomSheet, Constants.Obj.room)
                    bundle.putParcelableArrayList(Constants.Param.dataSrc, ArrayList(it))
                    val bottomSheetFrag = BottomSheetSelectionFragment.newInstance(bundle)
                    bottomSheetFrag.show(fragmentManager!!, Constants.FragmentName.bottomSheetFragment)
                    isCallApi = false
                }
            }
        }

        mMainActivityViewModel?.roomByCampus?.observe(viewLifecycleOwner, mRoomSubscriber)
    }

    private fun initListener() {
        tvTypeCheckIn.setOnClickListener(this)
        tvCampus.setOnClickListener(this)
        tvRoom.setOnClickListener(this)
        btnStart.setOnClickListener(this)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvTypeCheckIn -> {
                isCallApi = true
                mListener?.onSelection(Constants.Obj.typeCheckIn, null)
            }
            R.id.tvCampus -> {
                isCallApi = true
                mListener?.onSelection(Constants.Obj.campus, null)
            }
            R.id.tvRoom -> {
                if (tvCampus.text.isNotBlank() && tvTypeCheckIn.text.isNotBlank()) {
                    isCallApi = true
                    mListener?.onSelection(Constants.Obj.room, idCampus)
                }
            }
            R.id.btnStart -> {
                if (tvCampus.text.isNotBlank() && tvTypeCheckIn.text.isNotBlank() && tvRoom.text.isNotBlank()) {
                    startAutomaticallyCheckIn()
                }
            }
        }
    }


    private fun startAutomaticallyCheckIn() {
        val intent = Intent(context, AutoCheckInActivity::class.java)
        intent.putExtra(Constants.Param.roomId, tvRoom.text.toString().trim())
        startActivity(intent)
    }

    fun updateUI(bundle: Bundle?) {
        when (bundle?.getString(Constants.Param.dataType)) {
            Constants.Obj.campus -> {
                val data: Campus = bundle.getParcelable(Constants.Param.dataSelected)!!
                tvCampus.text = data.name
                idCampus = data.id
            }
            Constants.Obj.room -> {
                val data: Room = bundle.getParcelable(Constants.Param.dataSelected)!!
                tvRoom.text = data.name
            }
            Constants.Obj.typeCheckIn -> {
                val data: CheckInTypeData = bundle.getParcelable(Constants.Param.dataSelected)!!
                tvTypeCheckIn.text = data.name
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?): CheckingFragment {
            val fragment = CheckingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
