package com.dev.fitface.repository

import androidx.lifecycle.LiveData
import com.dev.fitface.api.api_utils.ApiResponse
import com.dev.fitface.api.api_utils.Resource
import com.dev.fitface.api.models.auth.LoginInput
import com.dev.fitface.api.models.auth.LoginResponse
import com.dev.fitface.api.models.campus.CampusResponse
import android.content.Context
import com.dev.fitface.api.api_utils.AppExecutor
import com.dev.fitface.api.api_utils.LiveDataCallAdapterFactory
import com.dev.fitface.api.models.course.CourseResponse
import com.dev.fitface.api.models.face.CheckInRequest
import com.dev.fitface.api.models.face.CheckInResponse
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.api.models.face.FaceResponse
import com.dev.fitface.api.models.report.*
import com.dev.fitface.api.models.room.RoomResponse
import com.dev.fitface.api.service.MoodleService
import com.dev.fitface.utils.AppUtils
import com.dev.fitface.utils.Constants
import com.dev.fitface.utils.SharedPrefs
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MoodleRepository constructor(
    val context: Context,
    val base_url: String,
    val appExecutor: AppExecutor
) {

    private var moodleService: MoodleService
    val suffix = "/api/"
    val moodle = SharedPrefs.instance[Constants.Param.collection, String::class.java] ?: ""
    val token = SharedPrefs.instance[Constants.Param.token, String::class.java] ?: ""


    init {
        val liveDataCallAdapterFactory = LiveDataCallAdapterFactory()
        val callApiClient = AppUtils.createOkHttpClient()

        moodleService = Retrofit.Builder()
            .baseUrl(base_url + suffix)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(liveDataCallAdapterFactory)
            .client(callApiClient).build()
            .create(MoodleService::class.java)
    }

    companion object {
        private var INSTANCE: MoodleRepository? = null
        fun instance(context: Context, BASE_URL: String, appExecutors: AppExecutor) = INSTANCE
            ?: MoodleRepository(context, BASE_URL, appExecutors).also {
                INSTANCE = it
            }
    }

    fun postLogin(input: LoginInput): LiveData<Resource<LoginResponse>> {
        return  object : NetworkBoundResource<LoginResponse>(appExecutor, context, this.base_url){
            override fun saveCallResult(item: LoginResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<LoginResponse>> {
                return moodleService.postLogin(moodle,input)
            }
        }.asLiveData()
    }

    fun getCampus(): LiveData<Resource<CampusResponse>> {
        return  object : NetworkBoundResource<CampusResponse>(appExecutor, context, this.base_url){
            override fun saveCallResult(item: CampusResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<CampusResponse>> {
                return moodleService.getCampus(moodle,token)
            }
        }.asLiveData()
    }

    fun getReportByCourseId(id: Int): LiveData<Resource<ReportCheckInResponse>> {
        return object : NetworkBoundResource<ReportCheckInResponse>(appExecutor, context, this.base_url) {
            override fun saveCallResult(item: ReportCheckInResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<ReportCheckInResponse>> {
                return moodleService.getReportByCourseId(moodle, token, id)
            }
        }.asLiveData()
    }

    fun getSessionByCourseId(id: Int): LiveData<Resource<SessionReportResponse>> {
        return object : NetworkBoundResource<SessionReportResponse>(appExecutor, context, this.base_url) {
            override fun saveCallResult(item: SessionReportResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<SessionReportResponse>> {
                return moodleService.getSessionByCourseId(moodle, token, id)
            }
        }.asLiveData()
    }

    fun getSessionByStudentId(username: Int, courseid: Int): LiveData<Resource<SessionReportByStudentIdResponse>> {
        return object : NetworkBoundResource<SessionReportByStudentIdResponse>(appExecutor, context, this.base_url) {
            override fun saveCallResult(item: SessionReportByStudentIdResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<SessionReportByStudentIdResponse>> {
                return moodleService.getSessionByStudentId(moodle, token, username, courseid)
            }
        }.asLiveData()
    }

    fun getStudentInfo(username: String): LiveData<Resource<LoginResponse>> {
        return  object : NetworkBoundResource<LoginResponse>(appExecutor, context, this.base_url){
            override fun saveCallResult(item: LoginResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<LoginResponse>> {
                return moodleService.getStudentInfo(token, moodle,username)
            }
        }.asLiveData()
    }

    fun postUpdateAttendanceLog(sessionId: String, input: UpdateLogRequest): LiveData<Resource<UpdateLogResponse>> {
        return  object : NetworkBoundResource<UpdateLogResponse>(appExecutor, context, this.base_url){
            override fun saveCallResult(item: UpdateLogResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<UpdateLogResponse>> {
                return moodleService.postUpdateAttendanceLog(token, moodle, sessionId, input)
            }
        }.asLiveData()
    }

    fun getRoom(campus: String): LiveData<Resource<RoomResponse>> {
        return object : NetworkBoundResource<RoomResponse>(appExecutor, context, this.base_url) {
            override fun saveCallResult(item: RoomResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<RoomResponse>> {
                return moodleService.getRoom(moodle, token, campus)
            }
        }.asLiveData()
    }

    fun getTeacherSchedules(): LiveData<Resource<CourseResponse>> {
        return object : NetworkBoundResource<CourseResponse>(appExecutor, context, this.base_url) {
            override fun saveCallResult(item: CourseResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<CourseResponse>> {
                return moodleService.getTeacherSchedules(moodle, token)
            }
        }.asLiveData()
    }

    fun postCheckIn(id: Int, input: CheckInRequest): LiveData<Resource<CheckInResponse>> {
        return object : NetworkBoundResource<CheckInResponse>(appExecutor, context, this.base_url) {
            override fun saveCallResult(item: CheckInResponse) {
            }

            override fun createCall(): LiveData<ApiResponse<CheckInResponse>> {
                return moodleService.postCheckIn(moodle, token, id, input)
            }
        }.asLiveData()
    }
}