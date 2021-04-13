package com.example.app.activity_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.app.*
import com.google.crypto.tink.KeysetHandle
import kotlinx.android.synthetic.main.activity_main.*
import java.security.KeyStore
import javax.crypto.SecretKey

class MainActivity : AppCompatActivity() {

    var secretKey : SecretKey? = null
    var secretKey1 : SecretKey? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)


        if (keyStore.size() == 0) {
            supportFragmentManager.beginTransaction()
                .add(R.id.ll_fragment_registration, RegistrationFragment.newInstance(keyStore))
                .commit()
            //ll_fragment_registration.visibility = View.VISIBLE
            cl_navigation.visibility = View.GONE
        }


        //Collections.list(keyStore.aliases()).size.toString()
        //(keyStore.getKey(et_login.text.toString(), null) == null)

        btn_singin.setOnClickListener {
            secretKey = keyStore.getKey(et_login.text.toString(), null) as SecretKey?
            if (secretKey != null)
            //(keyStore.getKey(et_login.text.toString(), null) != null).toString()
            Toast.makeText(this, keyStore.size().toString(), Toast.LENGTH_SHORT).show()
        }

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
        intent.putExtra("KEY", secretKey)
        startActivity(intent)
        finish()
    }

    fun startIdentity(view: View) {
        val intent = Intent(this, IdentityActivity::class.java)
       startActivity(intent)
        finish()
    }
}
