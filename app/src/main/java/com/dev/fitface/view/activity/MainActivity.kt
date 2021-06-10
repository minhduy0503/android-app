package com.dev.fitface.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.api.models.campus.Campus
import com.dev.fitface.api.models.room.Room
import com.dev.fitface.data.CheckInTypeData
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants
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
    CheckingFragment.OnCheckInFragmentInteractionListener,
    BottomSheetSelectionFragment.OnBottomSheetSelectionFragmentInteractionListener,
    HomeFragment.OnHomeFragmentInteractionListener {

    private var homeFragment: HomeFragment? = null
    private var checkingFragment: CheckingFragment? = null
    private var profileFragment: ProfileFragment? = null

    private var typeCheckInData: List<CheckInTypeData>? = null
    private var roomId: Int? = null
    private var roomName: String? = null
    private var campusName: String? = null

    override fun setLoadingView(): View? {
        return findViewById(R.id.layoutLoading)
    }

    override fun setActivityView() {
        setContentView(R.layout.activity_main)
    }

    override fun setActivityName(): String {
        return Constants.ActivityName.mainActivity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initValue()
        setDefaultFragment(savedInstanceState)
        initListener()
    }

    private fun initValue() {
        typeCheckInData = CheckInTypeData.getAllType()
        callApiGetTeacherSchedules()
    }


    private fun initListener() {
        nav_bar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val bundle = Bundle()
                    homeFragment = HomeFragment.newInstance(bundle)
                    AppUtils.addFragmentWithAnimLeft(
                        supportFragmentManager.beginTransaction(),
                        R.id.fragmentContainerView,
                        homeFragment!!,
                        Constants.FragmentName.homeFragment
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_check_in -> {
                    val bundle = Bundle()
                    checkingFragment = CheckingFragment.newInstance(bundle)
                    AppUtils.addFragmentWithAnimLeft(
                        supportFragmentManager.beginTransaction(),
                        R.id.fragmentContainerView,
                        checkingFragment!!,
                        Constants.FragmentName.checkInFragment
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    val bundle = Bundle()
                    profileFragment = ProfileFragment.newInstance(bundle)
                    AppUtils.addFragmentWithAnimLeft(
                        supportFragmentManager.beginTransaction(),
                        R.id.fragmentContainerView,
                        profileFragment!!,
                        Constants.FragmentName.profileFragment
                    )

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
        viewModel.responseCourses.observe(this, {
            it?.resource?.data.let { data ->
                viewModel.courses.postValue(data)
            }
        })
    }

    private fun observerRoom() {
        viewModel.responseRoom.observe(this, {
            it?.resource?.data.let { data ->
                viewModel.roomByCampus.postValue(data)
            }
        })
    }

    private fun observerCampus() {
        viewModel.responseCampus.observe(this, {
            it?.resource?.data.let { data ->
                viewModel.campus.postValue(data)
            }
        })
    }


    private fun callApiGetTeacherSchedules() {
        viewModel.getTeacherSchedules()
    }

    private fun callApiGetCampus() {
        viewModel.getCampus()
    }

    private fun callApiGetRoom(campusId: String) {
        viewModel.getRoom(campusId)
    }


    override fun handleError(statusCode: Int?, message: String?, bundle: Bundle?) {

    }

    private fun setDefaultFragment(savedInstanceState: Bundle?) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, HomeFragment.newInstance(savedInstanceState))
            .commit()
    }

    override fun onCheckInFragmentInteraction(type: String, id: String?) {
        when (type) {
            Constants.Obj.typeCheckIn -> {
                val bundle = Bundle()
                bundle.putString(Constants.Param.typeBottomSheet, Constants.Obj.typeCheckIn)
                bundle.putParcelableArrayList(Constants.Param.dataSrc, ArrayList(typeCheckInData))
                val bottomSheetFrag = BottomSheetSelectionFragment.newInstance(bundle)
                bottomSheetFrag.show(
                    supportFragmentManager,
                    Constants.FragmentName.bottomSheetFragment
                )
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

    override fun onStartCheckIn(type: String) {
        when (type) {
            Constants.CheckInType.auto -> {
                val intent = Intent(this, AutoCheckInActivity::class.java)
                intent.putExtra(Constants.Param.campusName, campusName)
                intent.putExtra(Constants.Param.roomName, roomName)
                intent.putExtra(Constants.Param.roomId, roomId)
                startActivity(intent)
            }

            Constants.CheckInType.manual -> {
                val intent = Intent(this, ManualCheckInActivity::class.java)
                intent.putExtra(Constants.Param.campusName, campusName)
                intent.putExtra(Constants.Param.roomName, roomName)
                intent.putExtra(Constants.Param.roomId, roomId)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        // Disable back press button and gesture
    }

    override fun onBottomSheetSelectionFragmentInteraction(bundle: Bundle?) {
        when (bundle?.getString(Constants.Param.dataType)) {
            Constants.Obj.campus -> {
                val data: Campus? = bundle.getParcelable(Constants.Param.dataSelected)
                campusName = data?.name ?: ""
            }

            Constants.Obj.room -> {
                val data: Room? = bundle.getParcelable(Constants.Param.dataSelected)
                roomId = data?.id ?: -1
                roomName = data?.name ?: ""
            }
        }
        checkingFragment?.updateUI(bundle)
    }

    override fun onHomeFragmentInteraction(bundle: Bundle) {
        AppUtils.startActivityWithBundle(this, CourseDetailActivity::class.java, bundle)
    }


}