package com.dev.fitface.api.models.response

import com.dev.fitface.api.models.auth.User

data class LoginResponse(
        var status: Int?,
        var message: String?,
        var data: User?
)