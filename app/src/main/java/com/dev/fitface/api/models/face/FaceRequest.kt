package com.dev.fitface.api.models.face

/**
 * Created by Dang Minh Duy on 12,May,2021
 */
data class FaceRequest(
        var images: ArrayList<String>,
        var collection: String
)