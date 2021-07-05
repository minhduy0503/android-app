package com.dev.fitface.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.campus.Campus
import com.dev.fitface.api.models.campus.CampusResponse
import com.dev.fitface.api.models.course.Course
import com.dev.fitface.api.models.course.CourseResponse
import com.dev.fitface.api.models.room.Room
import com.dev.fitface.api.models.room.RoomResponse
import com.dev.fitface.repository.MoodleRepository

/**
 * Created by Dang Minh Duy on 13,May,2021
 */
class MainActivityViewModel constructor(application: Application) : BaseViewModel(application) {

    private val moodleRepository: MoodleRepository = MoodleRepository.instance(application, BASE_URL, appExecutor)

    var campus: MediatorLiveData<List<Campus>?> = MediatorLiveData<List<Campus>?>().also {
        it.value = null
    }

    var roomByCampus: MediatorLiveData<List<Room>?> = MediatorLiveData<List<Room>?>().also {
        it.value = null
    }

    var courses: MediatorLiveData<List<Course>?> = MediatorLiveData<List<Course>?>().also {
        it.value = null
    }

    val responseCampus = MediatorLiveData<Resource<CampusResponse>>()
    fun getCampus() {
        responseCampus.addSource(moodleRepository.getCampus()) { newData ->
            setResultData<Resource<CampusResponse>>(newData)?.let {
                responseCampus.value = it
            }
        }
    }

    val responseRoom = MediatorLiveData<Resource<RoomResponse>>()
    fun getRoom(campus: String) {
        responseRoom.addSource(moodleRepository.getRoom(campus)) { newData ->
            setResultData<Resource<RoomResponse>>(newData)?.let {
                responseRoom.value = it
            }
        }
    }

    val responseCourses = MediatorLiveData<Resource<CourseResponse>>()
    fun getSchedules() {
        responseCourses.addSource(moodleRepository.getSchedules()) { newData ->
            setResultData<Resource<CourseResponse>>(newData)?.let {
                responseCourses.value = it
            }
        }
    }
}