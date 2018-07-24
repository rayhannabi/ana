package com.example.rayhan.ana

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import com.example.rayhan.ana.extensions.NoteApplication
import com.example.rayhan.ana.extensions.getPriorityColor
import com.example.rayhan.ana.extensions.onTextChanged
import com.example.rayhan.ana.notes.Note
import com.example.rayhan.ana.notes.NoteRepository
import kotlinx.android.synthetic.main.activity_note_detail.*
import kotlinx.android.synthetic.main.note_priority_chooser_view.*
import java.util.*

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE = "EXTRA_NOTE"

        fun newIntent(context: Context) =
                Intent(context, NoteDetailActivity::class.java)

        fun newIntent(context: Context, note: Note): Intent {
            val intent = newIntent(context)
            intent.putExtra(EXTRA_NOTE, note)

            return intent
        }
    }

    private lateinit var note: Note

    private lateinit var noteRepository: NoteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        noteRepository = (application as NoteApplication).noteRepository
        note = intent.getParcelableExtra(EXTRA_NOTE) ?: Note("", 0)

        editNoteView.onTextChanged {
            note.text = it
            note.lastModified = Date().time
        }

        lowPriorityView.setOnClickListener { onPriorityChanged(0, it) }
        normalPriorityView.setOnClickListener { onPriorityChanged(1, it) }
        highPriorityView.setOnClickListener { onPriorityChanged(2, it) }
        urgentPriorityView.setOnClickListener { onPriorityChanged(3, it) }

        showNote(note)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    override fun onBackPressed() {
        if (note.text.isEmpty()) {
            deleteNote()
        } else {
            noteRepository.saveNote(note)
        }

        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_note_detail_activity, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_delete) {
            deleteNote()
            supportFinishAfterTransition()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun deleteNote() {
        noteRepository.deleteNote(note)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cancelRevealAnimation()
        }
    }

    private fun showNote(note: Note) {
        editNoteView.setText(note.text)
        noteCardView.setCardBackgroundColor(
                ContextCompat.getColor(editNoteView.context, note.getPriorityColor())
        )
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun onPriorityChanged(priority: Int, view: View?) {
        note.priority = priority
        showNote(note)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val centerX = (view!!.x + view.width / 2).toInt()
            showRevealAnimation(centerX)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showRevealAnimation(centerX: Int) {
        val width = noteCardView.width.toDouble()
        val height = noteCardView.height.toDouble()
        val endRadius = Math.hypot(width, height).toFloat()

        val revealAnimator = ViewAnimationUtils.createCircularReveal(
                noteCardView,
                centerX,
                0,
                0f,
                endRadius
        )

        noteCardView.visibility = View.VISIBLE
        revealAnimator.duration = 500
        revealAnimator.start()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun cancelRevealAnimation() {
        noteCardView.transitionName = null
    }
}
