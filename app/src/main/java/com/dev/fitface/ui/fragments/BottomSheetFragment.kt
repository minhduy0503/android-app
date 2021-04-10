package com.dev.fitface.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.fitface.R
import com.dev.fitface.models.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

/*        val gson = Gson()
        val listUserType: Type = object : TypeToken<List<User?>?>() {}.type

        val users: List<User> = gson.fromJson(jsonFileString, listUserType)*/
    }

    companion object {
        const val TAG = "BottomSheetFragment"
    }

}