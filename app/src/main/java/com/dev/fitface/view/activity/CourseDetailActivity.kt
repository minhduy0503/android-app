package com.dev.fitface.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.dev.fitface.R
import com.dev.fitface.adapter.ViewPaperAdapter
import com.dev.fitface.api.models.course.Course
import com.dev.fitface.api.models.report.ReportCheckIn
import com.dev.fitface.api.models.report.Student
import com.dev.fitface.api.models.report.UpdateLogRequest
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants
import com.dev.fitface.view.BaseActivity
import com.dev.fitface.view.customview.ToastMessage
import com.dev.fitface.view.fragments.EditCheckInFragment
import com.dev.fitface.view.fragments.SessionInCourseFragment
import com.dev.fitface.view.fragments.StudentInCourseDetailFragment
import com.dev.fitface.view.fragments.StudentInCourseFragment
import com.dev.fitface.viewmodel.CourseDetailActivityViewModel
import kotlinx.android.synthetic.main.activity_course_detail.*

class CourseDetailActivity : BaseActivity<CourseDetailActivityViewModel>(),
    StudentInCourseFragment.OnStudentInCourseFragmentInteractionListener,
    SessionInCourseFragment.OnSessionInCourseFragmentInteractionListener,
    StudentInCourseDetailFragment.OnStudentInCourseDetailFragmentInteractionListener,
    EditCheckInFragment.OnEditCheckInFragmentInteractionListener,
    View.OnClickListener {

    private var mPageAdapter: ViewPaperAdapter? = null
    private var studentInCourseFragment: StudentInCourseFragment? = null
    private var sessionInCourseFragment: SessionInCourseFragment? = null

    private var courseId: Int? = null
    private var courseName: String? = null
    private var studentId: Int? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initValue()
        initView()
        initListener()
    }

    private fun initListener() {
        btnBack.setOnClickListener(this)
        btnFilterByStudent.setOnClickListener(this)
        btnFilterBySession.setOnClickListener(this)

        viewPaperFilter?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        selectFirstTab()
                    }
                    1 -> {
                        selectSecondTab()
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }
        })
    }

    private fun initView() {
        tvCourseName.text = courseName
        initPageAdapter()
    }

    private fun initPageAdapter() {
        viewPaperFilter?.offscreenPageLimit = 2
        if (mPageAdapter == null) {
            mPageAdapter = ViewPaperAdapter(supportFragmentManager)
        }
        mPageAdapter?.clearFragment()
        val bundle = Bundle()
        studentInCourseFragment = StudentInCourseFragment.newInstance(bundle)
        sessionInCourseFragment = SessionInCourseFragment.newInstance(bundle)

        studentInCourseFragment?.let {
            mPageAdapter?.addFragment(it, Constants.FragmentName.studentInCourseFragment)
        }
        sessionInCourseFragment?.let {
            mPageAdapter?.addFragment(it, Constants.FragmentName.sessionInCourseFragment)
        }

        viewPaperFilter.adapter = mPageAdapter
        viewPaperFilter.setCurrentItem(0, false)
        selectFirstTab()
    }

    private fun selectFirstTab() {
        callApiGetReportByStudent()
        val constraintSet = ConstraintSet()
        constraintSet.clone(courseFilter)
        constraintSet.connect(
            R.id.lineUnderTitle,
            ConstraintSet.LEFT,
            R.id.tvFilterByStudent,
            ConstraintSet.LEFT,
            0
        )
        constraintSet.connect(
            R.id.lineUnderTitle,
            ConstraintSet.RIGHT,
            R.id.tvFilterByStudent,
            ConstraintSet.RIGHT,
            0
        )
        constraintSet.applyTo(courseFilter)
        tvFilterByStudent?.setTextColor(
            ContextCompat.getColor(
                this@CourseDetailActivity,
                R.color.orange
            )
        )
        tvFilterBySession?.setTextColor(
            ContextCompat.getColor(
                this@CourseDetailActivity,
                R.color.text_title_gray
            )
        )

    }

    private fun selectSecondTab() {
        callApiGetReportBySession()
        val constraintSet = ConstraintSet()
        constraintSet.clone(courseFilter)
        constraintSet.connect(
            R.id.lineUnderTitle,
            ConstraintSet.LEFT,
            R.id.tvFilterBySession,
            ConstraintSet.LEFT,
            0
        )
        constraintSet.connect(
            R.id.lineUnderTitle,
            ConstraintSet.RIGHT,
            R.id.tvFilterBySession,
            ConstraintSet.RIGHT,
            0
        )
        constraintSet.applyTo(courseFilter)
        tvFilterBySession?.setTextColor(
            ContextCompat.getColor(
                this@CourseDetailActivity,
                R.color.orange
            )
        )
        tvFilterByStudent?.setTextColor(
            ContextCompat.getColor(
                this@CourseDetailActivity,
                R.color.text_title_gray
            )
        )

    }


    private fun initValue() {
        intent?.extras?.let {
            val courseData: Course? = it.getParcelable(Constants.Param.dataSelected)
            courseId = courseData?.id
            courseName = courseData?.fullname
        }
    }

    private fun callApiGetReportByStudent() {
        courseId?.let { viewModel.getReportByCourseId(it) }
    }

    private fun callApiGetReportBySession() {
        courseId?.let { viewModel.getSessionByCourseId(it) }
    }

    private fun callApiGetReportDetailByStudent() {
        studentId?.let { viewModel.getSessionByStudentId(it, courseId!!) }
    }

    private fun callAPiGetStudentInfo(){
        studentId?.let { viewModel.getStudentInfo(studentId.toString()) }
    }

    private fun callApiPostUpdateLog(sessionId: String, input: UpdateLogRequest){
        viewModel.postUpdateAttendanceLog(sessionId, input)
    }

    override fun createViewModel(): CourseDetailActivityViewModel {
        return ViewModelProvider(this).get(CourseDetailActivityViewModel::class.java)
    }

    override fun setLoadingView(): View? {
        return layoutLoading
    }

    override fun setActivityView() {
        setContentView(R.layout.activity_course_detail)
    }

    override fun setActivityName(): String {
        return Constants.ActivityName.courseDetailActivity
    }


    override fun observeData() {
        observerReportByCourseId()
        observerSessionByCourseId()
        observerSessionByStudentId()
        observerStudentInfo()
        observerUpdateLog()
    }

    private fun observerUpdateLog() {
        viewModel.responseUpdateLog.observe(this, {
            it?.resource?.let { data ->
                when(data?.status){
                    200 -> {
                        ToastMessage.makeText(this, "Cập nhật thành công", ToastMessage.SHORT, ToastMessage.Type.SUCCESS.type).show()
                    }
                    else -> {
                        ToastMessage.makeText(this, "Cập nhật thất bại", ToastMessage.SHORT, ToastMessage.Type.ERROR.type).show()
                    }
                }
            }
        })
    }

    private fun observerStudentInfo() {
        viewModel.responseStudentInfo.observe(this, {
            it?.resource?.data?.let { data ->
                viewModel.studentInfo.postValue(data)
            }
        })
    }

    private fun observerSessionByStudentId() {
        viewModel.responseSessionByStudentId.observe(this, {
            it?.resource?.data?.let { data ->
                Log.i("Debug","OK")
                viewModel.sessionByStudentId.postValue(data)
            }
        })
    }

    private fun observerSessionByCourseId() {
        viewModel.responseSessionByCourseId.observe(this, {
            it?.resource?.data?.let { data ->
                viewModel.session.postValue(data)
            }
        })
    }

    private fun observerReportByCourseId() {
        viewModel.responseReportByCourseId.observe(this, {
            it?.resource?.data?.let { data ->
                Log.i("Debug","=OK")
                viewModel.report.postValue(data)
            }
        })
    }


    override fun fetchData() {

    }

    override fun handleError(statusCode: Int?, message: String?, bundle: Bundle?) {

    }

    private fun selectTab(positionInPager: Int) {
        viewPaperFilter?.let {
            if (it.adapter?.count ?: -1 > positionInPager) {
                viewPaperFilter?.setCurrentItem(positionInPager, true)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBack -> {
                onBackPressed()
            }

            R.id.btnFilterByStudent -> {
                selectFirstTab()
                selectTab(0)
            }

            R.id.btnFilterBySession -> {
                selectSecondTab()
                selectTab(1)
            }
        }
    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onStudentInCourseInteraction(bundle: Bundle) {
        val data: ReportCheckIn? = bundle.getParcelable(Constants.Param.dataSelected)

        // TODO: Missing
        studentId = data?.username?.toInt()
        callAPiGetStudentInfo()
        ///
        val fragment = StudentInCourseDetailFragment.newInstance(bundle)
        AppUtils.addFragmentWithAnimLeft(supportFragmentManager.beginTransaction(), R.id.fullscreenContent, fragment, Constants.FragmentName.studentInCourseDetailFragment)
    }

    override fun onSessionInCourseInteraction(bundle: Bundle) {


    }

    override fun onStudentInCourseDetailInteraction(bundle: Bundle) {
        when(bundle.getString(Constants.ActivityName.courseDetailActivity)){
            Constants.Param.close -> {
                onBackPressed()
            }
            Constants.Param.confirm -> {
                val frag = EditCheckInFragment.newInstance(bundle)
                frag.isCancelable = false
                frag.show(supportFragmentManager, "Fragment")
            }
        }
    }

    override fun onEditCheckInFragmentInteraction(bundle: Bundle?) {
        when(bundle?.getString(Constants.ActivityName.courseDetailActivity)){
            Constants.Param.confirm -> {
                val sessionId = bundle.getInt(Constants.Param.sessionId)
                val timeIn = bundle.getLong(Constants.Param.timeIn)
                val timeOut = bundle.getLong(Constants.Param.timeOut)
                val studentId = bundle.getString(Constants.Param.studentId)
                val checkStatus = bundle.getInt(Constants.Param.checkInStatus)
                val sts = Student(studentId, timeIn, timeOut, checkStatus)
                val req = UpdateLogRequest(listOf(sts))
                callApiPostUpdateLog(sessionId.toString(), req)
            }
        }
        callApiGetReportByStudent()
        callApiGetReportBySession()
    }

}