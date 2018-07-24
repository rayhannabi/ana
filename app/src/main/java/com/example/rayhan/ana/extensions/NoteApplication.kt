package com.example.rayhan.ana.extensions

import android.app.Application
import android.content.Context
import com.example.rayhan.ana.notes.NoteRepository
import com.example.rayhan.ana.notes.NoteRepositoryImplementation

class NoteApplication: Application() {

    val noteRepository: NoteRepository by lazy {
        NoteRepositoryImplementation(getSharedPreferences("notes", Context.MODE_PRIVATE))
    }
}