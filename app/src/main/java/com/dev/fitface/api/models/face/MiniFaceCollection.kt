package com.dev.fitface.api.models.face

class MiniFaceCollection {
    var bitmapLandmark: String? = ""
    var miniFaceArray: ArrayList<MiniFace>? = arrayListOf()
}

class MiniFace(bitmap: String){
    var bitmap: String? = bitmap
    var isSelected: Boolean = false
}