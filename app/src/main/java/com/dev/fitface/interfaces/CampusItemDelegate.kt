package com.dev.fitface.interfaces

import com.dev.fitface.models.Campus

interface CampusItemDelegate {
    fun onItemClick(value: Campus)
}