package com.dev.fitface.api.models.face

import com.dev.fitface.api.models.BaseResponse

/**
 * Created by Dang Minh Duy on 12,May,2021
 */
class FaceResponse : BaseResponse() {
    var data: List<Face>? = null
}

data class Face(
        var status: Int,
        var id: String,
        var username: String,
        var firstname: String,
        var lastname: String,
        var roleid: Int,
        var role: String,
        var shortname: String,
        var avatar: String
)