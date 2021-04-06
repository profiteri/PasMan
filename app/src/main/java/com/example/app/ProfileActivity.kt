package com.example.app

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.SwipeHelpers.ProfileSwipeHelper
import com.example.app.adapters.ItemAdapter
import com.example.app.adapters.ProfilesAdapter
import com.example.app.database.DatabaseProfile
import com.example.app.dialogs.DeleteProfile
import com.example.app.models.ProfileModel
import com.happyplaces.adapters.NotesAdapter
import com.happyplaces.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.activity_profile.*
import pl.kitek.rvswipetodelete.SwipeToEditCallback

class ProfileActivity : ButtonsFunctionality() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        settingsInNotes.setOnClickListener {
            rotate(settingsInNotes)
        }

        angle.setOnClickListener {
            selectMenu(angle, angle_image, layout_menu)
        }

        plus_image.setOnClickListener {
            plusButton(
                this.findViewById(R.id.plus_image), R.id.profile_big
                , plus_image, R.id.main_layout_profile, R.id.add_menu
            )
        }

        val ss = et_source
        val c = this
        ss.setOnFocusChangeListener { v, focus ->
            val ll: LinearLayout = v.parent as LinearLayout
            if(focus){
                ll.setBackgroundResource(R.drawable.add_text_input_focused)
            }
            if (!focus) {
                ll.setBackgroundResource(R.drawable.add_text_input)
            }
        }

        setupListOfDataIntoRecycleView()
    }

    fun addProfile(view: View) {
        val source = et_source.text.toString()
        val login = et_login.text.toString()
        val password = et_password.text.toString()
        val info = et_info.text.toString()
        val handler = DatabaseProfile(this)
        if (handler.addProfile(ProfileModel(0, source, login, password, info)) > -1){
            Toast.makeText(this, "Profile added", Toast.LENGTH_SHORT).show()
            et_source.text.clear()
            et_login.text.clear()
            et_password.text.clear()
            et_info.text.clear()
            setupListOfDataIntoRecycleView()
            plusButton(
                this.findViewById(R.id.plus_image), R.id.profile_big
                , plus_image, R.id.main_layout_profile, R.id.add_menu
            )
        }
    }

    fun deleteItem(profile: ProfileModel) {
        val handler = DatabaseProfile(this)
        if (handler.deleteProfile(profile) == -1) {
            Toast.makeText(this, "ErRorr", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
        setupListOfDataIntoRecycleView()
    }

    private fun getProfiles(): ArrayList<ProfileModel> {
        return DatabaseProfile(this).viewProfile()
    }

    private fun setupListOfDataIntoRecycleView() {
        if (getProfiles().size > 0) {
            rv_profiles.visibility = View.VISIBLE
            rv_profiles.layoutManager = LinearLayoutManager(this)
            rv_profiles.adapter = ProfilesAdapter(this, getProfiles())
        }
        else {
            rv_profiles.visibility = View.GONE
        }


        /*val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val a = viewHolder.itemView.layoutParams
                DeleteProfile(rv_profiles.adapter as ProfilesAdapter, viewHolder as ProfilesAdapter.ViewHolder).show(supportFragmentManager, "delete_dialog")
                //viewHolder.itemView.translationX = 0f
                //viewHolder.itemView.translationY = 0f
                    //viewHolder.itemView.x = super.params
                //viewHolder.itemView.layoutParams = (rv_profiles.adapter as ProfilesAdapter).params
               // val adapter = rv_profiles.adapter as ProfilesAdapter
                //adapter.deleteItem(viewHolder as ProfilesAdapter.ViewHolder)
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                viewHolder.itemView.x = super.params
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_profiles)*/

        var attached = false
        val context : Context  = this
        val deleteSwipeHandler1 = object : ProfileSwipeHelper() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //Toast.makeText(context, viewHolder.itemView.layoutParams.toString(), Toast.LENGTH_SHORT).show()

                //viewHolder.itemView.layoutParams.width = 0
                DeleteProfile(rv_profiles.adapter as ProfilesAdapter, viewHolder as ProfilesAdapter.ViewHolder).show(supportFragmentManager, "delete_dialog")
                //
                this.bol = true
                //this.clearView(rv_profiles, viewHolder)
                attached = true

                /*val anim = ObjectAnimator.ofFloat(viewHolder.foreground, View.TRANSLATION_X, 0f)
                anim.duration = 300
                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        viewHolder.foreground.isEnabled = false
                    }
                    override fun onAnimationEnd(animation: Animator?) {
                        viewHolder.foreground.isEnabled = true
                    }
                })


                anim.start()*/

                viewHolder.foreground.alpha = 1f

                /*val anim = Thread(Runnable {
                    viewHolder.foreground.animate().translationX(0f)
                })
                anim.start()
                anim.join()*/

                viewHolder.foreground.animate().translationX(0f)
                Thread.sleep(1000)

                setupListOfDataIntoRecycleView()
                //ItemTouchHelper(this).attachToRecyclerView(null)
            }
        }
        //if (!attached)
           ItemTouchHelper(deleteSwipeHandler1).attachToRecyclerView(rv_profiles)
        //else
        //   ItemTouchHelper(deleteSwipeHandler1).attachToRecyclerView(null)

    }

    class AnimationThread(view: View) : Runnable {
        val v = view
        override fun run() {
            v.animate().translationX(0f)
        }
    }

}