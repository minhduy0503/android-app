package com.dev.fitface.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.campus.Campus
import com.dev.fitface.api.models.campus.CampusResponse
import com.dev.fitface.api.models.course.Course
import com.dev.fitface.api.models.course.CourseResponse
import com.dev.fitface.api.models.room.Room
import com.dev.fitface.api.models.room.RoomResponse
import com.dev.fitface.repository.CampusRepository
import com.dev.fitface.repository.CourseRepository
import com.dev.fitface.repository.RoomRepository

/**
 * Created by Dang Minh Duy on 13,May,2021
 */
class MainActivityViewModel constructor(application: Application) : BaseViewModel(application) {

    private val campusRepository: CampusRepository = CampusRepository.instance(application, BASE_URL, appExecutor)
    private val roomRepository: RoomRepository = RoomRepository.instance(application, BASE_URL, appExecutor)
    private val courseRepository: CourseRepository = CourseRepository.instance(application, BASE_URL, appExecutor)

    var campus: MutableLiveData<List<Campus>?> = MutableLiveData<List<Campus>?>().also {
        it.value = null
    }

    var roomByCampus: MutableLiveData<List<Room>?> = MutableLiveData<List<Room>?>().also {
        it.value = null
    }

    var courses: MutableLiveData<List<Course>?> = MutableLiveData<List<Course>?>().also {
        it.value = null
    }

    val responseCampus = MediatorLiveData<Resource<CampusResponse>>()
    fun getCampus(token: String) {
        responseCampus.addSource(campusRepository.getCampus(token)) { newData ->
            setResultData<Resource<CampusResponse>>(newData)?.let {
                responseCampus.value = it
            }

        }
    }

    val responseRoom = MediatorLiveData<Resource<RoomResponse>>()
    fun getRoom(token: String, campus: String) {
        responseRoom.addSource(roomRepository.getRoom(token, campus)) { newData ->
            setResultData<Resource<RoomResponse>>(newData)?.let {
                responseRoom.value = it
            }

        }
    }

    val responseCourses = MediatorLiveData<Resource<CourseResponse>>()
    fun getTeacherSchedules(token: String) {
        responseCourses.addSource(courseRepository.getTeacherSchedules(token)) { newData ->
            setResultData<Resource<CourseResponse>>(newData)?.let {
                responseCourses.value = it
            }
        }
    }
}