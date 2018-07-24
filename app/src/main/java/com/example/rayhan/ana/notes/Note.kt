package com.example.rayhan.ana.notes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(var text: String,
                var priority: Int = 0,
                var lastModified: Long = Date().time,
                val id: String = UUID.randomUUID().toString()) : Parcelable