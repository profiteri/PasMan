package com.example.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.swipeHelpers.DeleteSwipe
import com.example.app.swipeHelpers.ProfileSwipeHelper
import com.example.app.swipeHelpers.SwipeParamsHolder
import com.example.app.crypto.Encrypter
import com.example.app.database.DatabaseIdentity
import com.example.app.models.IdentityModel
import com.happyplaces.adapters.IdentityAdapter
import kotlinx.android.synthetic.main.activity_identity.*

class IdentityActivity : ButtonsFunctionality() {


    private var updateFormOpened = false

    private var currentItem: IdentityAdapter.ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identity)
        supportActionBar?.title = "Your Identities"

        getIdentitiesListFromPrivateDB()
        iv_plus_image_identity.setOnClickListener {
            if (updateFormOpened) {
                currentItem?.foreground?.alpha = 1f
                rv_identities.layoutManager = LinearLayoutManager(this)
                updateFormOpened = false
            } else {
                clearText()
                btn_add_identity.setText(R.string.add)
            }
            plusButton(
                R.id.iv_identit12321313y,
                iv_plus_image_identity,
                R.id.main_layout_identity,
                R.id.ll_add_menu_identity,
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

        val encrypter = Encrypter(null)

        val model = IdentityModel(
            0,
            encrypter.encryptString(et_name_identity.text.toString()),
            Encrypter(encrypter.getIv()).encryptString(et_surname_identity.text.toString()),
            Encrypter(encrypter.getIv()).encryptString(et_street_identity.text.toString()),
            Encrypter(encrypter.getIv()).encryptString(et_apartment_identity.text.toString()),
            Encrypter(encrypter.getIv()).encryptString(et_country_identity.text.toString()),
            Encrypter(encrypter.getIv()).encryptString(et_postcode_identity.text.toString()),
            Encrypter(encrypter.getIv()).encryptString(et_phone_identity.text.toString()),
            Encrypter(encrypter.getIv()).encryptString(et_email_identity.text.toString()),
            encrypter.getIv()
        )

        if (!updateFormOpened) {
            if (identityHandler.addIdentity(model) > -1) {
                Toast.makeText(this, "Identity added", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            if (currentItem != null) {
                (rv_identities.adapter as IdentityAdapter).updateIdentity(currentItem!!, model)
            }
            updateFormOpened = false
        }
        clearText()
        val dbHandler = DatabaseIdentity(this)
        setupIdentitiesRecyclerView(dbHandler.getIdentitiesList())
        plusButton(
            R.id.iv_identity,
            iv_plus_image_identity,
            R.id.main_layout_identity,
            R.id.ll_add_menu_identity,
            true
        )
        getIdentitiesListFromPrivateDB()
    }

    private fun clearText() {
        et_name_identity.text?.clear()
        et_surname_identity.text?.clear()
        et_street_identity.text?.clear()
        et_apartment_identity.text?.clear()
        et_country_identity.text?.clear()
        et_postcode_identity.text?.clear()
        et_phone_identity.text?.clear()
        et_email_identity.text?.clear()
    }


    private fun setupIdentitiesRecyclerView(identitieslist: ArrayList<IdentityModel>) {
        rv_identities.layoutManager = LinearLayoutManager(this)
        rv_identities.setHasFixedSize(true)
        val identityAdapter = IdentityAdapter(this, identitieslist)
        rv_identities.adapter = identityAdapter

        val d = DeleteSwipe(SwipeParamsHolder(rv_identities, supportFragmentManager))
        ItemTouchHelper(d).attachToRecyclerView(rv_identities)

        val deleteSwipeHelperRight = object : ProfileSwipeHelper(ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                plusButton(
                    R.id.iv_identit12321313y,
                    iv_plus_image_identity,
                    R.id.main_layout_identity,
                    R.id.ll_add_menu_identity,
                    true
                )
                updateFormOpened = true
                et_name_identity.setText((viewHolder as IdentityAdapter.ViewHolder).name.text)
                et_surname_identity.setText(viewHolder.surname)
                et_street_identity.setText(viewHolder.street)
                et_apartment_identity.setText(viewHolder.app)
                et_country_identity.setText(viewHolder.country)
                et_postcode_identity.setText(viewHolder.postcode)
                et_phone_identity.setText(viewHolder.phone)
                et_email_identity.setText(viewHolder.email.text)
                btn_add_identity.setText(R.string.update)
                currentItem = viewHolder
            }
        }
        ItemTouchHelper(deleteSwipeHelperRight).attachToRecyclerView(rv_identities)
    }


    fun getIdentitiesListFromPrivateDB() {
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

}
