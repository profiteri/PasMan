package com.example.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.SwipeHelpers.DeleteSwipe
import com.example.app.SwipeHelpers.ProfileSwipeHelper
import com.example.app.SwipeHelpers.SwipeParamsHolder
import com.example.app.adapters.ProfilesAdapter
import com.example.app.database.DatabaseIdentity
import com.example.app.models.IdentityModel
import com.happyplaces.adapters.IdentityAdapter
import com.happyplaces.adapters.NotesAdapter
import kotlinx.android.synthetic.main.activity_identity.*
import kotlinx.android.synthetic.main.activity_profile.*

class IdentityActivity : ButtonsFunctionality() {


    private var updateFormOpened = false
    private var currentItem: NotesAdapter.ViewHolder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identity)
        supportActionBar?.title = "Your Identities"
        getIdentitiesListFromPrivateDB()
        iv_plus_image_identity.setOnClickListener {
            plusButton(
                this.findViewById(R.id.iv_plus_image_identity),
                R.id.iv_identit12321313y,
                iv_plus_image_identity,
                R.id.main_layout_identity,
                R.id.ll_add_menu_identity1,
                true
            )
        }

        btn_settingsInNotes_identity.setOnClickListener {
            rotate(btn_settingsInNotes_identity)
        }

        btn_angle_identity.setOnClickListener {
            selectMenu(btn_angle_identity, iv_angle_image_identity, ll_layout_menu_identity)
        }
    }

    fun addIdentity(view: View) {

        val identityHandler = DatabaseIdentity(this)
        val status =
            identityHandler.addIdentity(
                IdentityModel(
                    0,
                    et_name_identity1.text.toString(),
                    et_surname_identity1.text.toString(),
                    et_street_identity1.text.toString(),
                    et_apartement_identity1.text.toString(),
                    et_country_identity1.text.toString(),
                    et_postcode_identity1.text.toString(),
                    et_phone_identity1.text.toString(),
                    et_email_identity1.text.toString()
                )
            )
        if (status > -1) {
            Toast.makeText(this, "Identity added", Toast.LENGTH_SHORT).show()
            et_name_identity1.text?.clear()
            et_surname_identity1.text?.clear()
            et_street_identity1.text?.clear()
            et_apartement_identity1.text?.clear()
            et_country_identity1.text?.clear()
            et_postcode_identity1.text?.clear()
            et_phone_identity1.text?.clear()
            et_email_identity1.text?.clear()
            val dbHandler = DatabaseIdentity(this)
            setupIdentitiesRecyclerView(dbHandler.getIdentitiesList())
            plusButton(
                this.findViewById(R.id.iv_plus_image_identity),
                R.id.iv_identity,
                iv_plus_image_identity,
                R.id.main_layout_identity,
                R.id.ll_add_menu_identity1,
                true
            )
        }
        getIdentitiesListFromPrivateDB()
    }


    private fun setupIdentitiesRecyclerView(identitieslist: ArrayList<IdentityModel>) {
        rv_identities.layoutManager = LinearLayoutManager(this)
        rv_identities.setHasFixedSize(true)


        val identityAdapter = IdentityAdapter(this, identitieslist)
        rv_identities.adapter = identityAdapter
        identityAdapter.setOnClickListener(object : IdentityAdapter.OnClickListener {
            override fun OnClick(position: Int, model: IdentityModel) {
                // val intent = Intent(this@IdentityActivity, NoteDetailsActivity::class.java)
                //intent.putExtra(NotesActivity.EXTRA_NOTES_DETAILS, model)
                //startActivity(intent)

            }
        })
        val d = DeleteSwipe(SwipeParamsHolder(rv_identities, supportFragmentManager))
        ItemTouchHelper(d).attachToRecyclerView(rv_identities)




    }


    private fun getIdentitiesListFromPrivateDB() {
        val dbHandler = DatabaseIdentity(this)
        val getIdentetiesList: ArrayList<IdentityModel> = dbHandler.getIdentitiesList()
        if (getIdentetiesList.size > 0) {
            rv_identities.visibility = View.VISIBLE
            setupIdentitiesRecyclerView(getIdentetiesList)
        } else {
            rv_identities.visibility = View.GONE
            //   tv_norecord_available.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_IDENTITY_ACTIVITY_REQUEST_CODE)
            if (resultCode == Activity.RESULT_OK) {
                getIdentitiesListFromPrivateDB()
            } else {
                Log.i("Activity", "Cancelled or Back pressed")
            }
    }

    companion object {
        var ADD_IDENTITY_ACTIVITY_REQUEST_CODE = 1
        var EXTRA_IDENTITIES_DETAILS = "extra identities details"
    }

    fun startCards(view: View) {
        val intent = Intent(this, CardActivity::class.java)
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
