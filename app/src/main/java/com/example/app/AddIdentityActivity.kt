package com.example.app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app.database.DatabaseIdentity
import com.example.app.database.DatabaseNotes
import com.example.app.models.IdentityModel
import com.example.app.models.NoteModel
import kotlinx.android.synthetic.main.activity_add_identity.*
import kotlinx.android.synthetic.main.activity_add_note.*

class AddIdentityActivity : AppCompatActivity() {
    private var mIdentetiesDetails: IdentityModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_identity)
        if (intent.hasExtra(IdentityActivity.EXTRA_IDENTITIES_DETAILS)) {
            mIdentetiesDetails =
                intent.getSerializableExtra(IdentityActivity.EXTRA_IDENTITIES_DETAILS) as IdentityModel
        }
        et_name.setText(mIdentetiesDetails!!.name)
        et_surname.setText(mIdentetiesDetails!!.surname)
        et_street.setText(mIdentetiesDetails!!.street)
        et_app.setText(mIdentetiesDetails!!.app)
        et_country.setText(mIdentetiesDetails!!.contry)
        et_phone.setText(mIdentetiesDetails!!.phoneNumber)
        et_email.setText(mIdentetiesDetails!!.email)
        /* if (mNotesDetails != null) {
             btn_add.setText("Update")
             supportActionBar?.title = "Edit Note"
             et_node_title.setText(mNotesDetails!!.titel)
             et_note_body.setText(mNotesDetails!!.text)

         } else {
             supportActionBar?.title = "Create new Note"
         }
         btn_add.setOnClickListener(this)
         btn_cancel.setOnClickListener(this)


         */
        btn_ok_identity.setOnClickListener {
            val identity = IdentityModel(
                if (mIdentetiesDetails == null) 0 else mIdentetiesDetails!!.id,
                et_name.text.toString(),
                et_surname.text.toString(),
                et_street.text.toString(),
                et_app.text.toString(),
                et_country.text.toString(),
                et_phone.text.toString(),
                et_postcode.text.toString(),
                et_email.text.toString()
            )
            val dbHandler = DatabaseIdentity(this)
            if (mIdentetiesDetails == null) {
                val addIdenResult = dbHandler.addIdentity(identity)
                if (addIdenResult > 0) {
                    setResult(Activity.RESULT_OK)
                }
            } else {
                val updateNoteResult = dbHandler.updateIdentity(identity)
                if (updateNoteResult > 0) {
                    setResult(Activity.RESULT_OK)
                }
            }

            val intent = Intent(this, IdentityActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


/* R.id.btn_cancel -> {
     val intent = Intent(this, NotesActivity::class.java)
     startActivity(intent)
     finish()
 }

 */
}

/* R.id.btn_cancel -> {
     val intent = Intent(this, NotesActivity::class.java)
     startActivity(intent)
     finish()
 }

 */
