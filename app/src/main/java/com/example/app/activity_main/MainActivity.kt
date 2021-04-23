package com.example.app.activity_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.app.*
import kotlinx.android.synthetic.main.activity_main.*
import java.security.KeyStore

/*TODO 3. Copy design from profiles
       4. Design entry profiles
*/
class MainActivity : AppCompatActivity() {

    //var alias : String? = null
    val keyStore = KeyStore.getInstance("AndroidKeyStore")

    @Synchronized
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        keyStore.load(null)

        if (keyStore.size() == 0) {
            supportFragmentManager.beginTransaction()
                .add(R.id.ll_fragment_registration, RegistrationFragment.newInstance(keyStore))
                .commit()

            cl_navigation.visibility = View.GONE
        }
        else {
            supportFragmentManager.beginTransaction()
                .add(R.id.ll_fragment_registration, LoginFragment.newInstance())
                .commit()

            cl_navigation.visibility = View.GONE
        }

        /*btn_singin.setOnClickListener {
            if ((keyStore.getKey(et_login.text.toString(), null) as SecretKey?) != null) {
                Toast.makeText(this, "Password correct", Toast.LENGTH_SHORT).show()
                alias = et_login.text.toString()
            }

        }*/

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
        //intent.putExtra("KEY", alias)
        startActivity(intent)
        finish()
    }

    fun startIdentity(view: View) {
        val intent = Intent(this, IdentityActivity::class.java)
       startActivity(intent)
        finish()
    }
}
