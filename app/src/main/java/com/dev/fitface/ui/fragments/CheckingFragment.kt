package com.dev.fitface.ui.fragments

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
    private var isCampusDone: Boolean? = false
    private var isRoomDone: Boolean? = false
    private var savedCampusId: String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(view)
        getCampusData()
        initListener()
    }

    private fun initListener() {
        chooseCampus()
        chooseRoom()
        startChecking()
    }

    private fun chooseCampus() {
        tvCampus?.isEnabled = true
        tvCampus?.setOnClickListener {
            if (isCampusDone == true) {
                val bundle = Bundle()
                bundle.putString("DialogType", "Campus")
                bundle.putParcelableArrayList("Data", campusData)
                val bottomSheetFragment = BottomSheetFragment.newInstance(bundle)
                bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
            }
        }
    }

    private fun chooseRoom() {
        tvRoom?.isEnabled = true
        tvRoom?.setOnClickListener {
            if (isRoomDone == true) {
                val bundle = Bundle()
                bundle.putString("DialogType", "Room")
                bundle.putParcelableArrayList("Data", roomData)
                val bottomSheetFragment = BottomSheetFragment.newInstance(bundle)
                bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
            }
        }

    }

    private fun startChecking() {
        btnStart?.isEnabled = true
        btnStart?.setOnClickListener {
            if (tvCampus?.text.toString().isEmpty() || tvRoom?.text.toString().isEmpty()) {
                CustomToast.makeText(requireContext(), "You need choose a room to check-in", 400, CustomToast.WARNING).show()
            } else {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                val room: Room? = this.arguments?.getParcelable("selectedRoom")
                val roomId = room?.name
                intent.putExtra("room_checkin", roomId)
                startActivity(intent)
            }
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

    private fun getCampusData() {
        val token = SharedPrefs.instance["Token", String::class.java]
        service.getCampus(token)?.enqueue(object : Callback<CampusResponse?> {
            override fun onResponse(call: Call<CampusResponse?>, response: Response<CampusResponse?>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val status = body?.status

                    // Handle other code:
                    if (response.code() != 200) {
                        CustomToast.makeText(requireContext(), "Error", 400, CustomToast.ERROR).show()
                    }

                    // If status is 200 -> Login successfully
                    else if (status == 200) {
                        // Get campus:
                        campusData = body.data
                        isCampusDone = true
                    }
                }
            }

            override fun onFailure(call: Call<CampusResponse?>, t: Throwable) {
                CustomToast.makeText(requireContext(), "Error", 400, CustomToast.ERROR).show()
            }

        })
    }

    fun getRoomData(campusId: String) {
        savedCampusId = campusId
        val token = SharedPrefs.instance["Token", String::class.java]
        service.getRoom(token, campusId)?.enqueue(object : Callback<RoomResponse?> {
            override fun onResponse(call: Call<RoomResponse?>, response: Response<RoomResponse?>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val status = body?.status

                    // Handle other code:
                    if (response.code() != 200) {
                        CustomToast.makeText(requireContext(), "Error", 400, CustomToast.ERROR).show()
                    }

                    // If status is 200 -> Login successfully
                    else if (status == 200) {
                        // Get room:
                        roomData = body.data
                        isRoomDone = true
                    }
                }
            }

            override fun onFailure(call: Call<RoomResponse?>, t: Throwable) {
                CustomToast.makeText(requireContext(), "Error", 400, CustomToast.ERROR).show()
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
    }

}