package com.example.sqlitenotesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlitenotesapp.databinding.ActivityUpdateBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding :ActivityUpdateBinding
    private lateinit var db : NoteDataBase
    private var noteId : Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = NoteDataBase(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        noteId = intent.getIntExtra("note_id",-1)
        if(noteId == -1){
            finish()
            return
        }
        val note = db.getNoteById(noteId)
        binding.updateTitleEditTxt.setText(note.title)
        binding.updateContentEditTxt.setText(note.content)
        binding.updatesavebtn.setOnClickListener{
            val newTitle = binding.updateTitleEditTxt.text.toString()
            val newContent = binding.updateContentEditTxt.text.toString()
            if (newTitle.isNotEmpty() && newContent.isNotEmpty()) {
                val currentDate = getCurrentDate()
                val updatedNote = Note(noteId, newTitle, newContent, currentDate)
                db.updateNotes(updatedNote)
                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_LONG).show()
            }
        }


    }
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}