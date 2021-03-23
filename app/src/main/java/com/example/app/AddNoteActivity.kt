package com.example.app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.app.database.DatabaseHandler
import com.example.app.models.NoteModel
import kotlinx.android.synthetic.main.activity_add_note.*

class AddNoteActivity : AppCompatActivity(), View.OnClickListener {
    private var mNotesDetails: NoteModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        val btn_add = findViewById(R.id.btn_add) as Button
        val btn_cancel = findViewById(R.id.btn_cancel) as Button
        if (intent.hasExtra(NotesActivity.EXTRA_NOTES_DETAILS)) {
            mNotesDetails =
                intent.getSerializableExtra(NotesActivity.EXTRA_NOTES_DETAILS) as NoteModel
        }
        if (mNotesDetails != null) {
            btn_add.setText("Update")
            supportActionBar?.title = "Edit Note"
            et_node_title.setText(mNotesDetails!!.titel)
            et_note_body.setText(mNotesDetails!!.text)

        } else {
            supportActionBar?.title = "Create new Note"
        }
        btn_add.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_add -> {
                val tv_title = findViewById(R.id.et_node_title) as TextView
                val tv_text = findViewById(R.id.et_note_body) as TextView
                when {
                    tv_title.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Pls enter title", Toast.LENGTH_SHORT).show()
                    }
                    tv_text.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Pls enter text", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        val note = NoteModel(
                            if (mNotesDetails == null) 0 else mNotesDetails!!.id
                            ,
                            tv_title.text.toString()
                            , tv_text.text.toString()
                        )
                        val dbHandler = DatabaseHandler(this)
                        if (mNotesDetails == null) {
                            val addNoteResult = dbHandler.addNote(note)
                            if (addNoteResult > 0) {
                                setResult(Activity.RESULT_OK)
                            }
                        } else {
                            val updateNoteResult = dbHandler.updateNote(note)
                            if (updateNoteResult > 0) {
                                setResult(Activity.RESULT_OK)
                            }
                        }

                        val intent = Intent(this, NotesActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            R.id.btn_cancel -> {
                val intent = Intent(this, NotesActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


}



