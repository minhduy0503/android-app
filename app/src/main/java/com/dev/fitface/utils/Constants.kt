package com.dev.fitface.utils

class Constants {
    class CallAction {
        companion object {
            const val postLogin = "post-login"
            const val getStudentReport = "get-student-reports"
            const val getReportsById = "get-report-by-id"
            const val getCampus = "get-campus"
            const val getRoom = "get-room"
            const val getSchedules = "get-teacher-schedules"
            const val getReportBySessionId = "get-session-by-id"
            const val updateAttendanceLog = "post-update-attendance-log"
            const val getStudentInfo = "get-student"
            const val getFaceByStudentId = "get-face-by-student-id"
            const val postFaceByStudentId = "post-face-by-student-id"
            const val putFaceByStudentId = "put-face-by-student-id"
            const val deleteFaceByStudentId = "delete-face-by-student-id"
            const val postFeedback = "post-feedback"
            const val postFindFace = "post-find-face"
            const val postCheckIn = "post-check-in"
            const val retryCapture = "retry-capture"
            const val add = "add"
            const val del = "delete"
        }
    }

    class CameraMode {
        companion object {
            const val automatic = 1
            const val manual = 0
        }
    }

    class ActivityName {
        companion object {
            const val splashActivity = "SplashActivity"
            const val loginActivity = "LoginActivity"
            const val mainActivity = "MainActivity"
            const val autoCheckInActivity = "AutoCheckInActivity"
            const val manualCheckInActivity = "ManualCheckInActivity"
            const val courseDetailActivity = "CourseDetailActivity"
            const val manualCheckInResultActivity = "ManualCheckInResultActivity"
        }
    }

    class FragmentName {
        companion object {
            const val homeFragment = "HomeFragment"
            const val checkInFragment = "CheckInFragment"
            const val profileFragment = "ProfileFragment"
            const val bottomSheetFragment = "BottomSheetFragment"
            const val studentInCourseFragment = "StudentInCourseFragment"
            const val sessionInCourseFragment = "SessionInCourseFragment"
            const val checkInResultFragment = "CheckInResultFragment"
            const val checkInReportFragment = "CheckInReportFragment"
            const val reportFragment = "ReportFragment"
            const val studentInCourseDetailFragment = "StudentInCourseDetailFragment"
            const val manualCheckInResultFragment = "ManualCheckInResultFragment"
        }
    }

    class Param {
        companion object {
            const val token = "Token"
            const val typeBottomSheet= "BottomSheetType"
            const val dataType = "DataType"
            const val dataSrc = "Data Source"
            const val dataSelected = "SelectedData"
            const val roomId = "RoomID"
            const val roomName = "RoomName"
            const val campusName = "CampusName"
            const val studentId = "StudentId"
            const val retry = "Retry"
            const val report = "Report"
            const val confirm = "Confirm"
            const val close = "Close"
            const val course = "Course"
            const val collection = "Collection"
            const val checkInStatus = "CheckInStatus"
            const val sessionId = "SessionId"
            const val timeIn = "TimeIn"
            const val timeOut = "TimeOut"
            const val passCode = "Passcode"
            const val width = "Width"
            const val height = "Height"
            const val image = "Image"
            const val userTaken = "TakenUser"
            const val username = "Username"
            const val startCamera = "StartCamera"
            const val action = "Action"
        }
    }

    class CheckInType{
        companion object{
            const val auto = "Tự động"
            const val manual = "Thủ công"
        }
    }

    class Obj{
        companion object{
            const val campus = "Campus"
            const val room = "Room"
            const val typeCheckIn ="TypeCheckIn"
            const val course = "Course"
            const val report = "Report"
            const val session = "Session"
            const val errorAvatar = "https://courses.fit.hcmus.edu.vn/theme/image.php/academi/core/1624873944/u/f1"
            const val base64Str = "Base64"
        }
    }

}