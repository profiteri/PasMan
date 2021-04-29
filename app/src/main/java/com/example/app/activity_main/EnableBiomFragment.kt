package com.example.app.activity_main

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.animation.addListener
import com.example.app.ProfileActivity
import com.example.app.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_enable_biom.view.*
import java.security.KeyStore

class EnableBiomFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enable_biom, container, false)

        mainActivity = (activity as MainActivity)
        val anim = ObjectAnimator
            .ofFloat(view.cl_enable_biom_main,"alpha", 0f, 1f)
            .setDuration(300)
        anim.start()
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        view.authorize.setOnClickListener{
            if (keyStore.getKey(view.password.text.toString(), null) != null) {
                showBiometricPromptForEncryption()
            }
            else
                Toast.makeText(mainActivity, "Wrong password!", Toast.LENGTH_SHORT).show()
        }
        view.cancel.setOnClickListener{
            anim.addListener(
                onEnd = {mainActivity.supportFragmentManager.beginTransaction()
                    .remove(this)
                    .add(R.id.ll_fragment_registration, LoginFragment.newInstance())
                    .commit()}
            )
            anim.reverse()
        }
        return view
    }

    private fun showBiometricPromptForEncryption() {
        val canAuthenticate = BiometricManager.from(mainActivity.applicationContext)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(mainActivity, ::storeToken)
            val promptInfo = BiometricPromptUtils.createPromptInfo(mainActivity)
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun storeToken(authResult: BiometricPrompt.AuthenticationResult) {
        User.fakeToken?.let { token ->
            Log.d("EnableBiometricLogin", "The token from server is $token")
            mainActivity.applicationContext.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE).edit().putString(CIPHERTEXT_WRAPPER, token).apply()
        }
        //(activity as MainActivity).cl_navigation.visibility = View.VISIBLE
        mainActivity.supportFragmentManager.beginTransaction()
            .remove(this)
            .commit()
        startActivity(Intent(mainActivity, ProfileActivity::class.java))
        mainActivity.finish()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            EnableBiomFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}