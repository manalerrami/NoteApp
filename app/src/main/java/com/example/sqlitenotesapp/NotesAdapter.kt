package com.example.sqlitenotesapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class NotesAdapter(private var notes:List<Note>, context:Context) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private val db:NoteDataBase = NoteDataBase(context)
    class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleTextview : TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView : TextView = itemView.findViewById(R.id.contentTextView)
        val dateTextView :TextView = itemView.findViewById(R.id.dateTextView)
        val updateBtn : ImageView = itemView.findViewById(R.id.editNote)
        val deleteBtn : ImageView = itemView.findViewById(R.id.deleteNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent,false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
       val note = notes[position]
        holder.titleTextview.text = note.title
        holder.contentTextView.text = note.content
        holder.dateTextView.text = note.date
        holder.updateBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateActivity::class.java).apply {
                putExtra("note_id",note.id)
            }
            holder.itemView.context.startActivity(intent)

        }
        holder.deleteBtn.setOnClickListener {
            // Context for the AlertDialog
            val context = holder.itemView.context

            // Create an AlertDialog Builder
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Note") // Set the title for the dialog
            builder.setMessage("Are you sure you want to delete this note?") // Set the message to show

            // Adding the buttons
            builder.setPositiveButton("Yes") { dialog, which ->
                // User clicked "Yes" button, delete the note
                db.deleteNote(note.id)
                refreshData(db.getAllNotes())
                Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") { dialog, which ->
                // User clicked the "No" button, just dismiss the dialog
                dialog.dismiss()
            }

            // Create and show the AlertDialog
            val dialog = builder.create()
            dialog.show()
        }

    }
    fun refreshData(newNotes : List<Note>){
        notes = newNotes
        notifyDataSetChanged()
    }
}