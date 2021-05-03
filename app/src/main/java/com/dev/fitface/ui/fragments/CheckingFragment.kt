package com.dev.fitface.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dev.fitface.R
import com.dev.fitface.api.ApiService
import com.dev.fitface.models.Campus
import com.dev.fitface.models.Room
import com.dev.fitface.models.response.CampusResponse
import com.dev.fitface.models.response.RoomResponse
import com.dev.fitface.ui.CustomToast
import com.dev.fitface.ui.activity.CameraActivity
import com.dev.fitface.ui.activity.MainActivity
import com.dev.fitface.utils.SharedPrefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CheckingFragment : Fragment() {

    private var campusData: ArrayList<Campus>? = null
    private var roomData: ArrayList<Room>? = null
    private var tvCampus: TextView? = null
    private var tvRoom: TextView? = null
    private var btnStart: Button? = null

    private var service: ApiService = ApiService.create()


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

/*
    private fun getCampusData() {
        val c1 = Campus("LT", "Linh Trung", 0)
        val c2 = Campus("NVC", "Nguyễn Văn Cừ", 0)
        campusData = ArrayList()
        campusData?.addAll(listOf(c1, c2))
    }
*/

    private fun getCampusData(){
        val token = SharedPrefs.instance["Token", String::class.java]
        service?.getCampus(token)?.enqueue(object : Callback<CampusResponse?> {
            override fun onResponse(call: Call<CampusResponse?>, response: Response<CampusResponse?>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val status = body?.status

                    // Handle other code:
                    if (response.code() != 200) {
                        CustomToast.makeText(requireContext(), "Error", Toast.LENGTH_LONG, CustomToast.ERROR).show()
                    }

                    // If status is 200 -> Login successfully
                    else if (status == 200) {
                        // Get campus:
                        campusData = body.data
                    }
                }
            }

            override fun onFailure(call: Call<CampusResponse?>, t: Throwable) {
                CustomToast.makeText(requireContext(), "Error", Toast.LENGTH_LONG, CustomToast.ERROR).show()
            }

        })
    }

    private fun getRoomData(campusId: String) {
        /*val r1 = Room(1, "A101", "LT", 0)
        val r2 = Room(2, "B101", "LT", 0)
        val r3 = Room(3, "C101", "LT", 0)
        val r4 = Room(4, "D101", "NVC", 0)
        val r5 = Room(5, "E101", "NVC", 0)
        val r6 = Room(6, "F101", "NVC", 0)
        roomData = ArrayList()
        roomData?.addAll(listOf(r1, r2, r3, r4, r5, r6))*/
        val token = SharedPrefs.instance["Token", String::class.java]
        service?.getRoom(token, campusId)?.enqueue(object : Callback<RoomResponse?> {
            override fun onResponse(call: Call<RoomResponse?>, response: Response<RoomResponse?>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val status = body?.status

                    // Handle other code:
                    if (response.code() != 200) {
                        CustomToast.makeText(requireContext(), "Error", Toast.LENGTH_LONG, CustomToast.ERROR).show()
                    }

                    // If status is 200 -> Login successfully
                    else if (status == 200) {
                        // Get campus:
                        roomData = body.data
                    }
                }
            }

            override fun onFailure(call: Call<RoomResponse?>, t: Throwable) {
                CustomToast.makeText(requireContext(), "Error", Toast.LENGTH_LONG, CustomToast.ERROR).show()
            }

        })
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
        const val Tag: String = "checking_frag"
    }

}