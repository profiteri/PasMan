package com.example.app

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.SwipeHelpers.ProfileSwipeHelper
import com.example.app.adapters.ItemAdapter
import com.example.app.adapters.ProfilesAdapter
import com.example.app.database.DatabaseProfile
import com.example.app.dialogs.CancelException
import com.example.app.dialogs.DeleteProfile
import com.example.app.models.ProfileModel
import com.happyplaces.adapters.NotesAdapter
import com.happyplaces.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.activity_profile.*
import pl.kitek.rvswipetodelete.SwipeToEditCallback
import kotlin.concurrent.thread

open class ProfileActivity : ButtonsFunctionality() {

    private var currentItem : ProfilesAdapter.ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        settingsInProfile.setOnClickListener {
            rotate(settingsInProfile)
        }

        angle.setOnClickListener {
            selectMenu(angle, angle_image, layout_menu)
        }

        plus_image.setOnClickListener {

            et_source.setText("")
            et_login.setText("")
            et_password.setText("")
            et_info.setText("")
            add_button_profile.setText(R.string.add)
            plusButton(
                this.findViewById(R.id.plus_image), R.id.profile_big
                , plus_image, R.id.main_layout_profile, R.id.add_menu
            )
        }

        val ss = et_source
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
        if (add_button_profile.text == resources.getString(R.string.add)) {
            val source = et_source.text.toString()
            val login = et_login.text.toString()
            val password = et_password.text.toString()
            val info = et_info.text.toString()
            val handler = DatabaseProfile(this)
            if (handler.addProfile(ProfileModel(0, source, login, password, info)) > -1) {
                Toast.makeText(this, "Profile added", Toast.LENGTH_SHORT).show()
            }
        }

        else if (currentItem != null) {
            currentItem!!.source.text = et_source.text
            currentItem!!.login.text = et_login.text
            currentItem!!.password.text = et_password.text
            currentItem!!.info.text = et_info.text
            (rv_profiles.adapter as ProfilesAdapter).updateProfile(currentItem!!)
        }

        et_source.text.clear()
        et_login.text.clear()
        et_password.text.clear()
        et_info.text.clear()
        add_button_profile.setText(R.string.add)
        setupListOfDataIntoRecycleView()
        plusButton(
            this.findViewById(R.id.plus_image), R.id.profile_big
            , plus_image, R.id.main_layout_profile, R.id.add_menu
        )
    }

    fun deleteItem(profile: ProfileModel) {
        val handler = DatabaseProfile(this)
        if (handler.deleteProfile(profile) == -1) {
            Toast.makeText(this, "ErRorr", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
        setupListOfDataIntoRecycleView()
    }

    fun updateItem(profile: ProfileModel) {
        val handler = DatabaseProfile(this)
        if (handler.updateProfile(profile) == -1) {
            Toast.makeText(this, "ErRorr", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
        setupListOfDataIntoRecycleView()
    }

    private fun getProfiles(): ArrayList<ProfileModel> {
        return DatabaseProfile(this).viewProfile()
    }


    val context = this
    private fun setupListOfDataIntoRecycleView() {
        if (getProfiles().size > 0) {
            rv_profiles.visibility = View.VISIBLE
            rv_profiles.layoutManager = LinearLayoutManager(this)
            rv_profiles.adapter = ProfilesAdapter(this, getProfiles())
        }
        else {
            rv_profiles.visibility = View.GONE
        }


        val deleteSwipeHandlerLeft = object : ProfileSwipeHelper(ItemTouchHelper.LEFT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                DeleteProfile(
                    rv_profiles.adapter as ProfilesAdapter,
                    viewHolder as ProfilesAdapter.ViewHolder,
                    rv_profiles,
                    context
                ).showAndReturn(supportFragmentManager, "delete_dialog")

                //viewHolder.foreground.alpha = 1f
                //viewHolder.foreground.animate().translationX(0f)
                //rv_profiles.layoutManager = LinearLayoutManager(context)
            }
        }
        ItemTouchHelper(deleteSwipeHandlerLeft).attachToRecyclerView(rv_profiles)

        val deleteSwipeHelperRight = object : ProfileSwipeHelper(ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                plusButton(
                    findViewById(R.id.plus_image), R.id.profile_big
                    , plus_image, R.id.main_layout_profile, R.id.add_menu
                )
                et_source.setText((viewHolder as ProfilesAdapter.ViewHolder).source.text)
                et_login.setText(viewHolder.login.text)
                et_password.setText(viewHolder.password.text)
                et_info.setText(viewHolder.info.text)
                add_button_profile.setText(R.string.update)
                currentItem = viewHolder
            }
        }
        ItemTouchHelper(deleteSwipeHelperRight).attachToRecyclerView(rv_profiles)
    }


   /* @Synchronized fun getAnswer(viewHolder: ProfilesAdapter.ViewHolder) : Boolean {
        runOnUiThread(Runnable{
            val bo = DeleteProfile(
                rv_profiles.adapter as ProfilesAdapter,
                viewHolder)
                .showAndReturn(supportFragmentManager, "delete_dialog")
        })
    }*/
}