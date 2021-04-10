package com.dev.fitface.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream

fun getJsonFromAssets(context: Context, fileName: String): String? {
    var jsonStr: String
    try {

        var inputStream: InputStream = context.assets.open(fileName)
        var fileSize = inputStream.available()
        val buffer = ByteArray(fileSize)
        inputStream.read(buffer)
        inputStream.close()

        jsonStr = String(buffer, Charsets.UTF_8)
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
    return jsonStr
}