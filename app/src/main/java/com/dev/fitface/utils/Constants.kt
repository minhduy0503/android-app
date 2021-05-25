package com.dev.fitface.utils

class Constants {
    class ActionMain {
        companion object {
            const val callNextAction = "callNextAction"
            const val callAction = "callAction"
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

        }
    }

    class FragmentName {
        companion object {
            const val homeFragment = "HomeFragment"
            const val checkInFragment = "CheckInFragment"
            const val profileFragment = "ProfileFragment"
            const val bottomSheetFragment = "BottomSheetFragment"
            const val autoCheckInResultFragment = "AutoCheckInResultFragment"

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
            const val retry = "Retry"
            const val report = "Report"
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
        }
    }

}