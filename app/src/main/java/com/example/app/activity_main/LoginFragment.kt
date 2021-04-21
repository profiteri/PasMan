package com.example.app.activity_main

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.animation.addListener
import com.example.app.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private lateinit var biometricPrompt: BiometricPrompt
    private val biomenricEnabled
        get() = requireActivity().applicationContext
            .getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE)
            .getString(CIPHERTEXT_WRAPPER, null)

    lateinit var mainActivity : MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        val anim = ObjectAnimator
            .ofFloat(view.cl_login_main,"alpha", 0f, 1f)
            .setDuration(300)
        anim.start()

        mainActivity = (activity as MainActivity)
        view.btn_login.setOnClickListener {
            if (mainActivity.keyStore.getKey(et_enter_password.text.toString(), null) != null) {
                //mainActivity.alias = et_enter_password.text.toString()
                mainActivity.cl_navigation.visibility = View.VISIBLE
                mainActivity.supportFragmentManager.beginTransaction().remove(this)
                    .commit()
            }
            else Toast.makeText(mainActivity, "Wrong password", Toast.LENGTH_SHORT).show()
        }

        val canAuthenticate = BiometricManager.from(mainActivity)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            view.tv_use_biometrics.visibility = View.VISIBLE
            view.tv_use_biometrics.setOnClickListener {
                if (biomenricEnabled != null) {
                    showBiometricPromptForDecryption()
                } else {
                    anim.addListener(
                        onEnd = {
                            mainActivity.supportFragmentManager.beginTransaction()
                                .remove(this)
                                .add(R.id.ll_fragment_registration, EnableBiomFragment.newInstance())
                                .commit()
                        }
                    )
                    anim.reverse()
                    //startActivity(Intent(mainActivity, EnableBiometricLoginActivity::class.java))
                }
            }
        } else {
            view.tv_use_biometrics.visibility = View.INVISIBLE
        }

        return view
    }

   // override fun onResume() {
   //     super.onResume()
   //     mainActivity.supportFragmentManager.beginTransaction().remove(this)
   //         .commit()
   // }

    private fun showBiometricPromptForDecryption() {
            biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(
                    mainActivity,
                    ::success
                )
            val promptInfo = BiometricPromptUtils.createPromptInfo(mainActivity)
            biometricPrompt.authenticate(promptInfo)
    }

    private fun success(authResult: BiometricPrompt.AuthenticationResult) {
        mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
        mainActivity.cl_navigation.visibility = View.VISIBLE
        //mainActivity.alias = mainActivity.keyStore.aliases().nextElement()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}