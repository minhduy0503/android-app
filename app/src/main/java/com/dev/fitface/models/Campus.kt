package com.dev.fitface.models

class Campus {
    var id: String = ""
    var name: String = ""

    override fun toString(): String {
        return "$id - $name"
    }
}