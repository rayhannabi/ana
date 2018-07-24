package com.example.rayhan.ana

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.rayhan.ana.extensions.NoteApplication
import com.example.rayhan.ana.notes.Note
import com.example.rayhan.ana.notes.NoteRepository
import kotlinx.android.synthetic.main.activity_note_list.*

class NoteListActivity : AppCompatActivity() {

    private lateinit var noteRepository: NoteRepository
    private lateinit var adapter: NoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        noteRepository = (application as NoteApplication).noteRepository

        adapter = NoteListAdapter(
                noteRepository.getNotes(),
                object : NoteListAdapter.Listener {
                    override fun onNoteClick(view: View, note: Note) {
                        editNote(view, note)
                    }
                }
        )

        noteListView.adapter = adapter
        addNoteButton.setOnClickListener {
            addNote()
        }
    }

    override fun onStart() {
        super.onStart()

        adapter.update(noteRepository.getNotes())
    }

    private fun addNote() {
        val intent = NoteDetailActivity.newIntent(this)
        startActivity(intent)
    }

    private fun editNote(view: View, note: Note) {
        val intent = NoteDetailActivity.newIntent(this, note)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                view,
                ViewCompat.getTransitionName(view))
        ActivityCompat.startActivity(this, intent, options.toBundle())
    }
}
