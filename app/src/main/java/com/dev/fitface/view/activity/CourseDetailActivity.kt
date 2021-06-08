package com.dev.fitface.view.activity

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.dev.fitface.R
import com.dev.fitface.adapter.ViewPaperAdapter
import com.dev.fitface.api.models.course.Course
import com.dev.fitface.utils.Constants
import com.dev.fitface.view.BaseActivity
import com.dev.fitface.view.fragments.SessionInCourseFragment
import com.dev.fitface.view.fragments.StudentInCourseFragment
import com.dev.fitface.viewmodel.CourseDetailActivityViewModel
import kotlinx.android.synthetic.main.activity_course_detail.*

class CourseDetailActivity : BaseActivity<CourseDetailActivityViewModel>(), View.OnClickListener {

    private var mPageAdapter: ViewPaperAdapter? = null
    private var studentInCourseFragment: StudentInCourseFragment? = null
    private var sessionInCourseFragment: SessionInCourseFragment? = null

    private var courseId: Int? = null
    private var courseName: String? = null

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

}