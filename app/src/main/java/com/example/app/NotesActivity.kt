package com.example.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.app.database.DatabaseHandler
import com.example.app.models.NoteModel

class NotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        getNotesListFromPrivateDB()
        val btn_add = findViewById(R.id.btn_add_notesactivity) as Button
        btn_add.setOnClickListener {
            val intent = Intent(this,AddNoteActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getNotesListFromPrivateDB() {
        val dbHandler = DatabaseHandler(this)
        val getNotesList: ArrayList<NoteModel> = dbHandler.getNotesList()
        if (getNotesList.size > 0) {
            for (i in getNotesList) {
                Log.e("Title", i.titel)
                Log.e("Text", i.text)
            }
        }

    }
}