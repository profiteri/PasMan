package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
}