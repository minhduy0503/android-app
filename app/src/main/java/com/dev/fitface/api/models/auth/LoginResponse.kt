package com.dev.fitface.api.models.auth

import com.dev.fitface.api.models.BaseResponse
import com.dev.fitface.api.models.`object`.User

/**
 * Created by Dang Minh Duy on 09,May,2021
 */
class LoginResponse: BaseResponse() {
    var data: User? = null
}