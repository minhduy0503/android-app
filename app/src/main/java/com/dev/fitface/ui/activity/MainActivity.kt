package com.dev.fitface.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dev.fitface.R
import com.dev.fitface.models.Campus
import com.dev.fitface.models.Room
import com.dev.fitface.ui.fragments.BottomSheetFragment
import com.dev.fitface.ui.fragments.CheckingFragment
import com.dev.fitface.ui.fragments.HomeFragment
import com.dev.fitface.ui.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomSheetFragment.OnOptionDialogFragmentInteractionListener{

    private var menuItemSelected: MenuItem? = null
    private var menuSelectedIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDefaultFragment(savedInstanceState)
        nav_bar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun setDefaultFragment(savedInstanceState: Bundle?){

        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.main_frame, HomeFragment.newInstance())

        if (savedInstanceState != null){
            menuSelectedIndex = savedInstanceState.getInt(SELECTED_ITEM, 0)
            menuItemSelected = nav_bar.menu.findItem(menuSelectedIndex!!)
        } else {
            menuItemSelected = nav_bar.menu.getItem(0)
        }

        selectFragment(menuItemSelected)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener{ item ->
        selectFragment(item)
        return@OnNavigationItemSelectedListener true
    }

    private fun selectFragment(item: MenuItem?) {
        var fragment: Fragment? = null
        val fragmentClass: Class<*> = when (item?.itemId) {
            R.id.navigation_home -> HomeFragment::class.java
            R.id.navigation_check_in -> CheckingFragment::class.java
            R.id.navigation_profile -> ProfileFragment::class.java
            else -> HomeFragment::class.java
        }
        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val fragmentManager: FragmentManager = supportFragmentManager
        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit()
        }
    }

    override fun onBackPressed() {
        // Disable back press behaviour
    }

    override fun onOptionCampusDialogFragmentInteraction(bundle: Bundle?) {
        val data: Campus? = bundle?.getParcelable("selectedCampus")
        val fragment = supportFragmentManager.findFragmentById(R.id.main_frame) as CheckingFragment
        fragment.onCampusTextViewChange(data?.name)
    }

    override fun onOptionRoomDialogFragmentInteraction(bundle: Bundle?) {
        val data: Room? = bundle?.getParcelable("selectedRoom")
        val fragment = supportFragmentManager.findFragmentById(R.id.main_frame) as CheckingFragment
        fragment.onRoomTextViewChange(data?.name)
    }

    companion object{
        private const val SELECTED_ITEM: String = "selected_item"
    }
}