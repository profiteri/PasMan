package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app.models.NoteModel
import kotlinx.android.synthetic.main.activity_note_details.*


class NoteDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)
        var noteDetailsModel: NoteModel? = null
       // if (intent.hasExtra(NotesActivity.EXTRA_NOTES_DETAILS)) {
       //     noteDetailsModel =
       //         intent.getSerializableExtra(NotesActivity.EXTRA_NOTES_DETAILS) as NoteModel
       // }
        if (noteDetailsModel != null) {
            //setSupportActionBar()
            tv_text_details.text = noteDetailsModel.text
            tv_title_details.text = noteDetailsModel.titel

        }
    }
}