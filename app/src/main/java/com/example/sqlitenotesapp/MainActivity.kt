package com.example.sqlitenotesapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlitenotesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var db: NoteDataBase
    private lateinit var notesAdapter: NotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = NoteDataBase(this)
        notesAdapter = NotesAdapter(db.getAllNotes(),this)
        binding.noteRecycleView.layoutManager = LinearLayoutManager(this)
        binding.noteRecycleView.adapter = notesAdapter
        binding.addBtn.setOnClickListener {
            val intent = Intent(this,CreateNoteActivity::class.java)
            startActivity(intent)
        }
        val searcheditText = findViewById<EditText>(R.id.searcheditText)
        // Inside your activity where the RecyclerView and EditText are initialized

        searcheditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event?.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                searchNotes(v.text.toString())
                v.setText("")
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                true
            } else {
                false
            }
        }}

        private fun searchNotes(query: String) {
            val filteredNotes = db.searchNotes(query)
            if (filteredNotes.isEmpty()) {
                Toast.makeText(this, "No notes found.", Toast.LENGTH_SHORT).show()
            } else {
                notesAdapter.refreshData(filteredNotes)
            }
        }


        override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}