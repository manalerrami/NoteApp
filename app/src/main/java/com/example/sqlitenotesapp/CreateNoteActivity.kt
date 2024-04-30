package com.example.sqlitenotesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlitenotesapp.databinding.ActivityCreateNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateNoteActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCreateNoteBinding
    private lateinit var db:NoteDataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = NoteDataBase(this)
        binding.savebtn.setOnClickListener {
            val title = binding.titleEditTxt.text.toString()
            val content = binding.contentEditTxt.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val currentDate = getCurrentDate()  // Get the current date
                val note = Note(0, title, content, currentDate)  // Include date when creating a new note
                db.insertNote(note)
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                finish()  // Close the activity and return to the previous screen
            } else {
                Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_LONG).show()
            }}
    }
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}