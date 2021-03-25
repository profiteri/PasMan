package com.example.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_identity.*
import kotlinx.android.synthetic.main.activity_notes.*

class IdentityActivity : ButtonsFunctionality() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identity)
        iv_plus_image_identity.setOnClickListener {
            plusButton(
                this.findViewById(R.id.iv_plus_image_identity), R.id.iv_identity
                , iv_plus_image_identity, R.id.main_layout_identity, R.id.ll_add_menu_identity
            )
        }
        btn_settingsInNotes_identity.setOnClickListener {
            rotate(btn_settingsInNotes_identity)
        }

        btn_angle_identity.setOnClickListener {
            selectMenu(btn_angle_identity, iv_angle_image_identity, ll_layout_menu_identity)
        }
        /*iv_plus_image.setOnClickListener {
            plusButton(
                this.findViewById(R.id.iv_plus_image), R.id.iv_notes
                , iv_plus_image, R.id.main_layout_notes, R.id.ll_add_menu
            )
        }

         */
    }

    fun startCards(view: View) {
        val intent = Intent(this, Cards::class.java)
        startActivity(intent)
        finish()
    }

    fun startNotes(view: View) {
        val intent = Intent(this, NotesActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun startProfiles(view: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun startIdentity(view: View) {
        val intent = Intent(this, IdentityActivity::class.java)
        startActivity(intent)
        finish()
    }
}