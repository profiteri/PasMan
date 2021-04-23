package com.example.app

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowMetrics
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.SwipeHelpers.DeleteSwipe
import com.example.app.SwipeHelpers.ProfileSwipeHelper
import com.example.app.SwipeHelpers.SwipeParamsHolder
import com.example.app.adapters.ProfilesAdapter
import com.example.app.crypto.Encrypter
import com.example.app.database.DatabaseProfile
import com.example.app.models.ProfileModel
import kotlinx.android.synthetic.main.activity_profile.*


open class ProfileActivity : ButtonsFunctionality() {

    private var currentItem : ProfilesAdapter.ViewHolder? = null
    private var updateFormOpened = false

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
                , plus_image, R.id.main_layout_profile, R.id.add_menu, false
            )
            if (updateFormOpened) {
                currentItem?.foreground?.alpha = 1f
                rv_profiles.layoutManager = LinearLayoutManager(this)
                updateFormOpened = false
            }
        }

        val focusListener = View.OnFocusChangeListener {v, focus ->
            val ll: LinearLayout = v.parent as LinearLayout
            if(focus){
                ll.setBackgroundResource(R.drawable.add_text_input_focused)
            }
            if (!focus) {
                ll.setBackgroundResource(R.drawable.add_text_input)
            }
        }
        et_source.onFocusChangeListener = focusListener
        et_login.onFocusChangeListener = focusListener
        et_password.onFocusChangeListener = focusListener
        et_info.onFocusChangeListener = focusListener

        setupListOfDataIntoRecycleView()
    }

    @Synchronized
    fun addProfile(view: View) {
        val encrypter = Encrypter(null)
        val source = encrypter.encryptString(et_source.text.toString())
        val login = Encrypter(encrypter.getIv()).encryptString(et_login.text.toString())
        val password = Encrypter(encrypter.getIv()).encryptString(et_password.text.toString())
        val info = Encrypter(encrypter.getIv()).encryptString(et_info.text.toString())

        if (add_button_profile.text == resources.getString(R.string.add)) {
            val handler = DatabaseProfile(this)
            if (handler.addProfile(ProfileModel(0, source, login, password, info, encrypter.getIv())) > -1) {
                Toast.makeText(this, "Profile added", Toast.LENGTH_SHORT).show()
            }
        }
        else if (currentItem != null) {
            (rv_profiles.adapter as ProfilesAdapter)
                .updateProfile(currentItem!!, ProfileModel(-1, source, login, password, info, encrypter.getIv()))
            updateFormOpened = false
        }
        et_source.text.clear()
        et_login.text.clear()
        et_password.text.clear()
        et_info.text.clear()
        add_button_profile.setText(R.string.add)
        plusButton(
            this.findViewById(R.id.plus_image), R.id.profile_big,
            plus_image, R.id.main_layout_profile, R.id.add_menu, false
        )
        setupListOfDataIntoRecycleView()
    }

  /*  fun deleteItem(profile: ProfileModel) {
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
    }*/

    private fun getProfiles(): ArrayList<ProfileModel> {
        return DatabaseProfile(this).viewProfile()
    }


    val context = this
    fun setupListOfDataIntoRecycleView() {
        if (getProfiles().size > 0) {
            rv_profiles.visibility = View.VISIBLE
            rv_profiles.layoutManager = LinearLayoutManager(this)
            rv_profiles.adapter = ProfilesAdapter(this, getProfiles())
        }
        else {
            rv_profiles.visibility = View.GONE
        }

        val d = DeleteSwipe(SwipeParamsHolder(rv_profiles, supportFragmentManager))
        ItemTouchHelper(d).attachToRecyclerView(rv_profiles)

        val deleteSwipeHelperRight = object : ProfileSwipeHelper(ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                plusButton(
                    findViewById(R.id.plus_image), R.id.profile_big
                    , plus_image, R.id.main_layout_profile, R.id.add_menu, false
                )
                updateFormOpened = true
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

}