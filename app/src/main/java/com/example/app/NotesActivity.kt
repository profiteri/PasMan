package com.example.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.database.DatabaseHandler
import com.example.app.models.NoteModel
import com.happyplaces.adapters.NotesAdapter
import com.happyplaces.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_notes.*
import pl.kitek.rvswipetodelete.SwipeToEditCallback

class NotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = "Your Notes"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        getNotesListFromPrivateDB()
        iv_animation_cards.setOnClickListener {
            val intent = Intent(this, Cards::class.java)
            startActivity(intent)
            finish()
        }
        val btn_add = findViewById(R.id.btn_add_notesactivity) as Button
        btn_add.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_ACTIVITY_REQUEST_CODE)
            finish()
        }
        val bu = findViewById<ImageButton>(R.id.btn_settingsInNotes)
        bu.setOnClickListener {
            val aniRotate = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
            bu.startAnimation(aniRotate)
        }

        var visible = false
        var alfha: Float

        val angle = findViewById<Button>(R.id.btn_angle)
        angle.setOnClickListener {

            val image = findViewById<ImageView>(R.id.iv_angle_image)
            val animator = ObjectAnimator.ofFloat(image, View.TRANSLATION_Y, -10f)
            animator.repeatCount = 1
            animator.repeatMode = ObjectAnimator.REVERSE
            animator.duration = 100;
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    angle.isEnabled = false;
                }

                override fun onAnimationEnd(animation: Animator?) {
                    angle.isEnabled = true;
                }
            })
            animator.start()

            val layout = findViewById<LinearLayout>(R.id.ll_layout_menu)

            if (!visible) {
                alfha = 1f
                visible = true
            } else {
                alfha = 0f
                visible = false
            }
            val anim = ObjectAnimator.ofFloat(layout, View.ALPHA, alfha)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    layout.alpha = alfha
                }
            })
            anim.duration = 50
            anim.start()
        }
        /*btn_animation_cards.setOnClickListener {
            val intent = Intent(this, Cards::class.java)
            startActivity(intent)
            finish()
        }

         */
    }

    private fun setupNotesRecyclerView(noteslist: ArrayList<NoteModel>) {
        rv_notes_list.layoutManager = LinearLayoutManager(this)
        rv_notes_list.setHasFixedSize(true)


        val notesAdapter = NotesAdapter(this, noteslist)
        rv_notes_list.adapter = notesAdapter
        notesAdapter.setOnClickListener(object : NotesAdapter.OnClickListener {
            override fun OnClick(position: Int, model: NoteModel) {
                val intent = Intent(this@NotesActivity, NoteDetailsActivity::class.java)
                intent.putExtra(EXTRA_NOTES_DETAILS, model)
                startActivity(intent)

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
        val dbHandler = DatabaseHandler(this)
        val getNotesList: ArrayList<NoteModel> = dbHandler.getNotesList()
        if (getNotesList.size > 0) {
            rv_notes_list.visibility = View.VISIBLE
            tv_norecord_available.visibility = View.GONE
            setupNotesRecyclerView(getNotesList)
        } else {
            rv_notes_list.visibility = View.GONE
            tv_norecord_available.visibility = View.VISIBLE
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