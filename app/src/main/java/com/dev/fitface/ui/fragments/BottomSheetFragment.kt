package com.dev.fitface.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.fitface.R
import com.dev.fitface.R.string
import com.dev.fitface.adapter.CampusAdapter
import com.dev.fitface.models.Campus
import com.dev.fitface.models.Room
import com.dev.fitface.utils.BottomSheetType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_checkin.*
import java.lang.reflect.Type


class BottomSheetFragment(type: BottomSheetType, fragment: Fragment) : BottomSheetDialogFragment() {

    private var campuses: ArrayList<Campus>? = null
    private var rooms: ArrayList<Room>? = null
    private var campusAdapter: CampusAdapter? = null
    private var mFragment: Fragment? = fragment as CheckinFragment
    private var type: BottomSheetType? = type


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

        when(type){
            BottomSheetType.CAMPUS -> {
                setupBottomSheetForCampus()
            }
            BottomSheetType.ROOM -> {
                setupBottomSheetForRoom()
            }
        }

        return view
    }

    private fun fetchDataFromJson(): ArrayList<Campus>{
        val jsonStr = requireContext()
                .applicationContext
                .assets
                .open("campus.json")
                .bufferedReader(Charsets.UTF_8)
                .use { it.readText()}
        Log.i(TAG, jsonStr)

        val listCampus: Type = object: TypeToken<List<Campus>>() {}.type
        return Gson().fromJson(jsonStr, listCampus)
    }


    private fun setupBottomSheetForCampus(){
        campuses = fetchDataFromJson()
        view?.rvCampus?.layoutManager = LinearLayoutManager(context)
        campusAdapter = CampusAdapter(context!!, campuses!!, mFragment as CheckinFragment)
        view?.rvCampus?.itemAnimator = DefaultItemAnimator()
        view?.rvCampus?.adapter = campusAdapter
        campusAdapter?.notifyDataSetChanged()
    }

    private fun setupBottomSheetForRoom(){
        when(mFragment?.view?.findViewById<TextView>(R.id.tvCampus)?.text){
            resources.getString(string.nvc) -> {

            }

            resources.getString(string.lt) -> {

            }
        }
    }


    companion object {
        const val TAG = "BottomSheetFragment"
        @JvmStatic
        fun newInstance(type: BottomSheetType, fragment: Fragment): BottomSheetFragment = BottomSheetFragment(type, fragment)
    }

}