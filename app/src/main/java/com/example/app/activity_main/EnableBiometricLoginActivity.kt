package com.example.app.activity_main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.example.app.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_enable_biometric_login.*
import java.security.KeyStore

class EnableBiometricLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enable_biometric_login)

        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        authorize.setOnClickListener{
           if (keyStore.getKey(password.text.toString(), null) != null) {
               showBiometricPromptForEncryption()
           }
            else
               Toast.makeText(this, "Wrong password!", Toast.LENGTH_SHORT).show()
        }
        cancel.setOnClickListener{
            finish()
        }
    }

    private fun showBiometricPromptForEncryption() {
        val canAuthenticate = BiometricManager.from(applicationContext)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(this, ::storeToken)
            val promptInfo = BiometricPromptUtils.createPromptInfo(this)
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun storeToken(authResult: BiometricPrompt.AuthenticationResult) {
            User.fakeToken?.let { token ->
                Log.d("EnableBiometricLogin", "The token from server is $token")
                applicationContext.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE).edit().putString(CIPHERTEXT_WRAPPER, token).apply()
            }
        finish()
    }
}
