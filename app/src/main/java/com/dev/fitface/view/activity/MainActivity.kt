package com.dev.fitface.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dev.fitface.R
import com.dev.fitface.api.models.`object`.Campus
import com.dev.fitface.api.models.`object`.Room
import com.dev.fitface.view.fragments.BottomSheetFragment
import com.dev.fitface.view.fragments.CheckingFragment
import com.dev.fitface.view.fragments.HomeFragment
import com.dev.fitface.view.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomSheetFragment.OnOptionDialogFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDefaultFragment(savedInstanceState)
        nav_bar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    private fun setDefaultFragment(savedInstanceState: Bundle?){
        if(savedInstanceState == null){
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_frame, HomeFragment.newInstance())
                    .commit()
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener{ item ->
        when (item.itemId){
            R.id.navigation_home -> {
                val homeFragment = HomeFragment.newInstance()
                openFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_check_in -> {

                val checkingFragment = CheckingFragment.newInstance()
                openFragment(checkingFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                val profileFragment = ProfileFragment.newInstance()
                openFragment(profileFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction
                .replace(R.id.main_frame, fragment)
                .commit()
    }

    override fun onBackPressed() {
        // Disable back press behaviour
    }

    override fun onOptionCampusDialogFragmentInteraction(bundle: Bundle?) {
        val data: Campus? = bundle?.getParcelable("selectedCampus")
        val fragment = supportFragmentManager.findFragmentById(R.id.main_frame) as CheckingFragment
        fragment.onCampusTextViewChange(data?.name)
        fragment.getRoomData(data?.id!!)
    }

    override fun onOptionRoomDialogFragmentInteraction(bundle: Bundle?) {
        val data: Room? = bundle?.getParcelable("selectedRoom")
        val fragment = supportFragmentManager.findFragmentById(R.id.main_frame) as CheckingFragment
        fragment.onRoomTextViewChange(data?.name)
        fragment.arguments = bundle
    }

}