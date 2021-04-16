package com.dev.fitface.models

class Campus(id: String, name: String) {
    var id: String = ""
    var name: String = ""

    override fun toString(): String {
        return "$id - $name"
    }
}