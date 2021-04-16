package com.dev.fitface.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dev.fitface.R
import com.dev.fitface.models.Campus
import com.dev.fitface.ui.activity.CameraActivity
import com.dev.fitface.utils.BottomSheetType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_checkin.*
import kotlinx.android.synthetic.main.fragment_checkin.view.*
import java.lang.reflect.Type


class CheckinFragment : Fragment(), AdapterView.OnItemClickListener {

    @SuppressLint("ResourceType")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_checkin, container, false)

        var campus = fetchDataFromJson()

        var adapterView = ArrayAdapter(requireContext().applicationContext, android.R.layout.simple_spinner_dropdown_item, campus)
        adapterView.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        initBottomSheet(view)

        return view
    }

    private fun fetchDataFromJson(): ArrayList<String>{
        val jsonStr = requireContext()
                .applicationContext
                .assets
                .open("campus.json")
                .bufferedReader(Charsets.UTF_8)
                .use { it.readText()}
        Log.i(BottomSheetFragment.TAG, jsonStr)
        val listCampus: Type = object: TypeToken<List<Campus>>() {}.type
        return Gson().fromJson(jsonStr, listCampus)
    }




    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent!!.getItemAtPosition(position).toString()
        // Showing selected spinner item
        // Showing selected spinner item
        Toast.makeText(parent!!.context, "Selected: $item", Toast.LENGTH_LONG).show()
    }




    private fun initBottomSheet(view: View){
        view.tvCampus.setOnClickListener {
            showBottomSheetDialog(BottomSheetType.CAMPUS)
        }

        view.tvRoom.setOnClickListener {
            showBottomSheetDialog(BottomSheetType.ROOM)
        }

        view.btnStart.setOnClickListener{
            val intent = Intent(this.context, CameraActivity::class.java)
            startActivity(intent)
        }
    }


    private fun showBottomSheetDialog(type: BottomSheetType){
        when(type){
            BottomSheetType.CAMPUS -> {
                val bottomSheetFragment = BottomSheetFragment.newInstance(BottomSheetType.CAMPUS, this)
                bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
            }

            BottomSheetType.ROOM -> {
                val bottomSheetFragment = BottomSheetFragment.newInstance(BottomSheetType.ROOM, this)
                bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.tag)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): CheckinFragment = CheckinFragment()
    }

}