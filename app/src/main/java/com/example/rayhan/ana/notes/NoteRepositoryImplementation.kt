package com.example.rayhan.ana.notes

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NoteRepositoryImplementation(private val sharedPreferences: SharedPreferences) : NoteRepository {

    companion object {
        const val NOTES_KEY = "NOTES_KEY"
    }

    private val gson = Gson()

    private inline fun <reified T> Gson.fromJson(json: String) : T = this.fromJson(json,
            object: TypeToken<T>() {}.type)

    override fun getNotes(): List<Note> {
        val notesString = sharedPreferences.getString(NOTES_KEY, null)

        if (notesString != null) {
            return gson.fromJson(notesString)
        }

        return emptyList()
    }

    override fun saveNote(note: Note) {
        val index = getNotes().indexOfFirst { n -> n.id == note.id }
        val notes: List<Note>

        if (index < -1) {
            notes = getNotes().toMutableList()
            notes[index] = note
        } else {
            notes = listOf(note) + getNotes()
        }

        save(notes)
    }

    override fun deleteNote(note: Note) {
        val notes = getNotes().filter { n -> n.id != note.id }
        save(notes)
    }

    private fun save(notes: List<Note>) {
        val editor = sharedPreferences.edit()
        editor.putString(NOTES_KEY, gson.toJson(notes))
        editor.commit()
    }
}