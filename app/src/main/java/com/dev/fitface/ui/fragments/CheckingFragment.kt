package com.dev.fitface.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dev.fitface.R
import com.dev.fitface.models.Campus
import com.dev.fitface.models.Room
import com.dev.fitface.ui.activity.CameraActivity


class CheckingFragment : Fragment() {

    private var campusData: ArrayList<Campus>? = null
    private var roomData: ArrayList<Room>? = null
    private var tvCampus: TextView? = null
    private var tvRoom: TextView? = null
    private var btnStart: Button? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(view)
        initListener()
    }

    init {
        getCampusData()
//        getRoomData()
    }

    private fun initListener() {
        tvCampus?.isEnabled = true
        tvCampus?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("DialogType", "Campus")
            bundle.putParcelableArrayList("Data", campusData)
            val bottomSheetFragment = BottomSheetFragment.newInstance(bundle)
            bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
        }
        btnStart?.isEnabled = true
        btnStart?.setOnClickListener{
            val intent = Intent(requireContext(), CameraActivity::class.java)
            startActivity(intent)
        }
    }


    private fun bind(view: View) {
        tvCampus = view.findViewById(R.id.tvCampus)
        tvRoom = view.findViewById(R.id.tvRoom)
        btnStart = view.findViewById(R.id.btnStart)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkin, container, false)
    }

/*    private fun initCampusBottomSheet() {
        val bundle = Bundle()
        bundle.putString("DialogType", "Campus")
        bundle.putParcelableArrayList("Data", campusData)

    }*/

/*    private fun initBottomSheet(view: View) {
        view.tvCampus.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("DialogType", "Campus")
            bundle.putParcelableArrayList("Data", campusData)
            val bottomSheetFragment = BottomSheetFragment.newInstance(bundle)
            bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
        }

        view.tvRoom.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("DialogType", "Room")
            bundle.putSerializable("Data", roomData)
            val bottomSheetFragment = BottomSheetFragment.newInstance(bundle)
            bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
        }

        view.btnStart.setOnClickListener {
            val intent = Intent(this.context, CameraActivity::class.java)
            startActivity(intent)
        }
    }*/

    private fun getCampusData() {
        val c1 = Campus("LT", "Linh Trung", 0)
        val c2 = Campus("NVC", "Nguyễn Văn Cừ", 0)
        campusData = ArrayList()
        campusData?.addAll(listOf(c1, c2))
    }

    private fun getRoomData(campus: String?) {
        val r1 = Room(1, "A101", "LT", 0)
        val r2 = Room(2, "B101", "LT", 0)
        val r3 = Room(3, "C101", "LT", 0)
        val r4 = Room(4, "D101", "NVC", 0)
        val r5 = Room(5, "E101", "NVC", 0)
        val r6 = Room(6, "F101", "NVC", 0)
        roomData = ArrayList()
        roomData?.addAll(listOf(r1, r2, r3, r4, r5, r6))
    }

    fun onCampusTextViewChange(campus: String?) {
        tvCampus?.text = campus
    }

    fun onRoomTextViewChange(room: String?) {
        tvRoom?.text = room
    }

    companion object {
        @JvmStatic
        fun newInstance(): CheckingFragment = CheckingFragment()
    }

}