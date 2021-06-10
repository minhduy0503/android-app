package com.dev.fitface.utils

class Constants {
    class CallApiAction {
        companion object {
            const val postLogin = "post-login"
            const val getStudentReport = "get-student-reports"
            const val getReportsById = "get-report-by-id"
            const val getCampus = "get-campus"
            const val getRoom = "get-room"
            const val getTeacherSchedules = "get-teacher-schedules"
            const val getReportBySessionId = "get-session-by-id"
            const val updateAttendanceLog = "post-update-attendance-log"
            const val getStudentInfo = "get-student"
            const val getFaceByStudentId = "get-face-by-student-id"
            const val postFaceByStudentId = "post-face-by-student-id"
            const val putFaceByStudentId = "put-face-by-student-id"
            const val deleteFaceByStudentId = "delete-face-by-student-id"
            const val postFeedback = "post-feedback"
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
        }
    }

    class CheckInType{
        companion object{
            const val auto = "Automatic"
            const val manual = "Manual"
        }
    }

    class Obj{
        companion object{
            const val campus = "Campus"
            const val room = "Room"
            const val typeCheckIn ="TypeCheckIn"
            const val course = "Course"
            const val faceStr = "FaceString64"
            const val report = "Report"
            const val session = "Session"
        }
    }

}