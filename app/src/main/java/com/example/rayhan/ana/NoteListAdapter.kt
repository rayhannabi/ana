package com.example.rayhan.ana

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.rayhan.ana.extensions.getPriorityColor
import com.example.rayhan.ana.extensions.inflate
import com.example.rayhan.ana.notes.Note
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.note_item.*
import java.text.SimpleDateFormat
import java.util.*

class NoteListAdapter(private var noteList: List<Note>,
                      private val listener: Listener) : RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
            NoteViewHolder(parent.inflate(R.layout.note_item))

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
            holder.bind(noteList[position], listener)

    override fun getItemCount(): Int = noteList.size

    fun update(noteList: List<Note>) {
        this.noteList = noteList
        notifyDataSetChanged()
    }

    class NoteViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {

        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())

        fun bind(note: Note, listener: Listener) {

            noteTextView.text = note.text
            noteDateView.text = simpleDateFormat.format(Date(note.lastModified))

            noteCardView.setBackgroundColor(
                    ContextCompat.getColor(noteCardView.context, note.getPriorityColor()))
            noteCardView.setOnClickListener {
                listener.onNoteClick(noteCardView, note)
            }
        }
    }

    interface Listener {
        fun onNoteClick(view: View, note: Note)
    }
}