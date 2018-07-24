package com.example.rayhan.ana.notes

interface NoteRepository {
    fun getNotes() : List<Note>
    fun saveNote(note: Note)
    fun deleteNote(note: Note)
}