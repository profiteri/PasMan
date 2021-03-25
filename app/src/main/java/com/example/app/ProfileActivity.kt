package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.adapters.ItemAdapter
import com.example.app.adapters.ProfilesAdapter
import com.example.app.database.DatabaseProfile
import com.example.app.models.ProfileModel
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.activity_profile.*

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
    }
}