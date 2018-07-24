package com.example.rayhan.ana.extensions

import com.example.rayhan.ana.R
import com.example.rayhan.ana.notes.Note

fun Note.getPriorityColor() : Int {
    return when (priority) {
        0 -> R.color.colorLowPriority
        1 -> R.color.colorNormalPriority
        2 -> R.color.colorHighPriority
        else -> R.color.colorUrgentPriority
    }
}