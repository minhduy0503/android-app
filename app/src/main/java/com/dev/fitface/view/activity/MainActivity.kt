package com.dev.fitface.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.data.CheckInTypeData
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.SharedPrefs
import com.dev.fitface.view.BaseActivity
import com.dev.fitface.view.fragments.BottomSheetSelectionFragment
import com.dev.fitface.view.fragments.CheckingFragment
import com.dev.fitface.view.fragments.HomeFragment
import com.dev.fitface.view.fragments.ProfileFragment
import com.dev.fitface.viewmodel.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_loading.view.*


class MainActivity : BaseActivity<MainActivityViewModel>(),
        CheckingFragment.OnSelectionInteractionListener,
        BottomSheetSelectionFragment.OnOptionBottomSheetInteractionListener,
        HomeFragment.OnHomeFragmentInteractionListener {

    private var homeFragment: HomeFragment? = null
    private var checkingFragment: CheckingFragment? = null
    private var profileFragment: ProfileFragment? = null

    private var typeCheckInData: List<CheckInTypeData>? = null

    override fun setLoadingView(): View? {
        return findViewById(R.id.layoutLoadingLogin)
    }

    override fun setActivityView() {
        setContentView(R.layout.activity_main)
    }

    override fun setActivityName(): String {
        return Constants.ActivityName.mainActivity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        setDefaultFragment(savedInstanceState)
        initListener()
    }

    private fun initData() {
        typeCheckInData = CheckInTypeData.getAllType()
        callApiGetTeacherSchedules()
    }


    private fun initListener() {
        nav_bar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val bundle = Bundle()
                homeFragment = HomeFragment.newInstance(bundle)
                AppUtils.addFragmentWithAnimLeft(supportFragmentManager.beginTransaction(), R.id.main_frame, homeFragment!!, Constants.FragmentName.homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_check_in -> {
                val bundle = Bundle()
                checkingFragment = CheckingFragment.newInstance(bundle)
                AppUtils.addFragmentWithAnimLeft(supportFragmentManager.beginTransaction(), R.id.main_frame, checkingFragment!!, Constants.FragmentName.checkInFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                val bundle = Bundle()
                profileFragment = ProfileFragment.newInstance(bundle)
                AppUtils.addFragmentWithAnimLeft(supportFragmentManager.beginTransaction(), R.id.main_frame, profileFragment!!, Constants.FragmentName.profileFragment)

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun createViewModel(): MainActivityViewModel {
        return ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    override fun fetchData() {

    }


    override fun observeData() {
        observerCourses()
        observerCampus()
        observerRoom()
    }

    private fun observerCourses() {
        viewModel.responseCourses.observe(this, Observer {
            it?.resource?.data.let {
                viewModel.courses.postValue(it)
            }
        })
    }

    private fun observerRoom() {
        viewModel.responseRoom.observe(this, Observer {
            it?.resource?.data.let {
                viewModel.roomByCampus.postValue(it)
            }
        })
    }

    private fun observerCampus() {
        viewModel.responseCampus.observe(this, Observer {
            it?.resource?.data.let {
                viewModel.campus.postValue(it)
            }
        })




    }

    override fun handleError(statusCode: Int?, message: String?, bundle: Bundle?) {

    }

    private fun setDefaultFragment(savedInstanceState: Bundle?) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_frame, HomeFragment.newInstance(savedInstanceState))
                .commit()
    }

    override fun onSelection(type: String, id: String?) {
        when (type) {
            Constants.Obj.typeCheckIn -> {
                val bundle = Bundle()
                bundle.putString(Constants.Param.typeBottomSheet, Constants.Obj.typeCheckIn)
                bundle.putParcelableArrayList(Constants.Param.dataSrc, ArrayList(typeCheckInData))
                val bottomSheetFrag = BottomSheetSelectionFragment.newInstance(bundle)
                bottomSheetFrag.show(supportFragmentManager, Constants.FragmentName.bottomSheetFragment)
            }
            Constants.Obj.campus -> {
                callApiGetCampus()
            }
            Constants.Obj.room -> {
                if (id != null) {
                    callApiGetRoom(id)
                }
            }
        }
    }

    private fun callApiGetTeacherSchedules() {
        val token = SharedPrefs.instance[Constants.Param.token, String::class.java] ?: ""
        viewModel.getTeacherSchedules(token)
    }

    private fun callApiGetCampus() {
        val token = SharedPrefs.instance[Constants.Param.token, String::class.java] ?: ""
        viewModel.getCampus(token)
    }

    private fun callApiGetRoom(campusId: String) {
        val token = SharedPrefs.instance[Constants.Param.token, String::class.java] ?: ""
        viewModel.getRoom(token, campusId)
    }

    override fun onBackPressed() {
        // Disable back press button and gesture
    }

    override fun onSelectedInteraction(bundle: Bundle?) {
        checkingFragment?.updateUI(bundle)
    }

    override fun onHomeFragmentInteraction(bundle: Bundle) {

    }


}