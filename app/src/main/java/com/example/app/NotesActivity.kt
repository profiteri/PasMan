package com.example.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.database.DatabaseNotes
import com.example.app.models.NoteModel
import com.happyplaces.adapters.NotesAdapter
import com.happyplaces.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_notes.*
import pl.kitek.rvswipetodelete.SwipeToEditCallback
import com.example.app.database.DatabaseCards
import com.example.app.models.CardModel

class NotesActivity : ButtonsFunctionality() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = "Your Notes"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        getNotesListFromPrivateDB()

        /* val btn_add = findViewById(R.id.btn_add_notesactivity) as Button
         btn_add.setOnClickListener {
             val intent = Intent(this, AddNoteActivity::class.java)
             startActivityForResult(intent, ADD_NOTE_ACTIVITY_REQUEST_CODE)
             finish()
         }

         */
        iv_plus_image.setOnClickListener {
            plusButton(
                this.findViewById(R.id.iv_plus_image), R.id.iv_notes
                , iv_plus_image, R.id.main_layout_notes, R.id.ll_add_menu
            )
        }
        btn_settingsInNotes.setOnClickListener {
            rotate(btn_settingsInNotes)
        }

        btn_angle.setOnClickListener {
            selectMenu(btn_angle, iv_angle_image, ll_layout_menu)
        }
        /*iv_plus_image.setOnClickListener {
            plusButton(
                this.findViewById(R.id.iv_plus_image), R.id.iv_notes
                , iv_plus_image, R.id.main_layout_notes, R.id.ll_add_menu
            )
        }

         */

    }

    fun addNote(view: View) {
        val notesHandler = DatabaseNotes(this)
        val status =
            notesHandler.addNote(NoteModel(0, et_title1.text.toString(), et_text1.text.toString()))
        if (status > -1) {
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show()
            findViewById<EditText>(R.id.et_title).text.clear()
            findViewById<EditText>(R.id.et_text).text.clear()

            val dbHandler = DatabaseNotes(this)

            setupNotesRecyclerView(dbHandler.getNotesList())
            plusButton(
                this.findViewById(R.id.iv_plus_image), R.id.iv_notes
                , iv_plus_image, R.id.main_layout_notes, R.id.ll_add_menu
            )
            /*val intent = Intent(this,NotesActivity::class.java)
            startActivity(intent)
            finish()

             */

        }
    }

    private fun setupNotesRecyclerView(noteslist: ArrayList<NoteModel>) {
        rv_notes_list.layoutManager = LinearLayoutManager(this)
        rv_notes_list.setHasFixedSize(true)


        val notesAdapter = NotesAdapter(this, noteslist)
        rv_notes_list.adapter = notesAdapter
        notesAdapter.setOnClickListener(object : NotesAdapter.OnClickListener {
            override fun OnClick(position: Int, model: NoteModel) {
         //       val intent = Intent(this@NotesActivity, NoteDetailsActivity::class.java)
          //      intent.putExtra(EXTRA_NOTES_DETAILS, model)
          //      startActivity(intent)

            }
        })
        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_notes_list.adapter as NotesAdapter
                adapter.notifyEditItem(
                    this@NotesActivity, viewHolder.adapterPosition,
                    ADD_NOTE_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_notes_list)
        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_notes_list.adapter as NotesAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_notes_list)
    }

    private fun getNotesListFromPrivateDB() {
        val dbHandler = DatabaseNotes(this)
        val getNotesList: ArrayList<NoteModel> = dbHandler.getNotesList()
        if (getNotesList.size > 0) {
            rv_notes_list.visibility = View.VISIBLE
            //tv_norecord_available.visibility = View.GONE
            setupNotesRecyclerView(getNotesList)
        } else {
            rv_notes_list.visibility = View.GONE
        //    tv_norecord_available.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_ACTIVITY_REQUEST_CODE)
            if (resultCode == Activity.RESULT_OK) {
                getNotesListFromPrivateDB()
            } else {
                Log.i("Activity", "Cancelled or Back pressed")
            }
    }

    companion object {
        var ADD_NOTE_ACTIVITY_REQUEST_CODE = 1
        var EXTRA_NOTES_DETAILS = "extra notes details"
    }
}