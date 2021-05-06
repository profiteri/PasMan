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
import com.example.app.swipeHelpers.DeleteSwipe
import com.example.app.swipeHelpers.ProfileSwipeHelper
import com.example.app.swipeHelpers.SwipeParamsHolder
import com.example.app.crypto.Encrypter
import com.example.app.database.DatabaseNotes
import com.example.app.models.NoteModel
import com.happyplaces.adapters.NotesAdapter
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.activity_profile.*

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
            if (updateFormOpened) {
                currentItem?.foreground?.alpha = 1f
                rv_notes_list.layoutManager = LinearLayoutManager(this)
                updateFormOpened = false
            }
            else {
                et_title.setText("")
                et_text.setText("")
                add_button.setText(R.string.add)
            }
            plusButton(
                R.id.iv_notes,
                iv_plus_image, R.id.main_layout_notes,
                R.id.add_menu1, false
            )
        }

        btn_settingsInNotes.setOnClickListener {
            rotate(btn_settingsInNotes)
        }

        btn_angle.setOnClickListener {
            selectMenu(btn_angle, iv_angle_image, ll_layout_menu)
        }
    }

    fun addNote(view: View) {

        val encrypter = Encrypter(null)
        val title = encrypter.encryptString(et_title.text.toString())
        val text = Encrypter(encrypter.getIv()).encryptString(et_text.text.toString())

        val notesHandler = DatabaseNotes(this)
        if (!updateFormOpened) {
            if (notesHandler.addNote(NoteModel(0, title, text, encrypter.getIv())) > -1) {
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            if (currentItem != null)
                (rv_notes_list.adapter as NotesAdapter).updateNote(currentItem!!,
                    NoteModel(-1, title, text, encrypter.getIv())
                )
            updateFormOpened = false
        }

        et_title.text?.clear()
        et_text.text?.clear()
        add_button.setText(R.string.add)
        setupNotesRecyclerView(notesHandler.getNotesList())
        plusButton(
            R.id.iv_notes, iv_plus_image,
            R.id.main_layout_notes, R.id.add_menu1, false
        )
        getNotesListFromPrivateDB()
    }

    private var updateFormOpened = false
    private var currentItem : NotesAdapter.ViewHolder? = null

    private fun setupNotesRecyclerView(noteslist: ArrayList<NoteModel>) {
        rv_notes_list.layoutManager = LinearLayoutManager(this)
        rv_notes_list.setHasFixedSize(true)

        val notesAdapter = NotesAdapter(this, noteslist)
        rv_notes_list.adapter = notesAdapter

        val d = DeleteSwipe(SwipeParamsHolder(rv_notes_list, supportFragmentManager))
        ItemTouchHelper(d).attachToRecyclerView(rv_notes_list)

        val deleteSwipeHelperRight = object : ProfileSwipeHelper(ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                plusButton(
                    R.id.iv_notes, iv_plus_image,
                    R.id.main_layout_notes, R.id.add_menu1, false
                )
                updateFormOpened = true
                et_title.setText((viewHolder as NotesAdapter.ViewHolder).title.text)
                et_text.setText(viewHolder.text)
                add_button.setText(R.string.update)
                currentItem = viewHolder
            }
        }
        ItemTouchHelper(deleteSwipeHelperRight).attachToRecyclerView(rv_notes_list)
    }

    fun getNotesListFromPrivateDB() {
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
    }
}