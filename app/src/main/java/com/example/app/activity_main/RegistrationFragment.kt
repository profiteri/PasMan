package com.example.app.activity_main

import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.app.R
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.config.TinkConfig
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*
import java.security.Key
import java.security.KeyStore
import javax.crypto.KeyGenerator

private var ARG_PARAM1 : KeyStore? = null

class RegistrationFragment : Fragment() {

    var keyStore : KeyStore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            keyStore = ARG_PARAM1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)
        view.btn_create_password.setOnClickListener {

            val paramsBuilder = KeyGenParameterSpec.Builder(
                et_create_password.text.toString(),
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            paramsBuilder.apply {
                setRandomizedEncryptionRequired(false)
                setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                setKeySize(256)
                //setUserAuthenticationRequired(true)
            }
            val keyGenParams = paramsBuilder.build()
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore")
            keyGenerator.init(keyGenParams)
            (activity as MainActivity).secretKey = keyGenerator.generateKey()
            (activity as MainActivity).cl_navigation.visibility = View.VISIBLE
            (activity as MainActivity).supportFragmentManager.beginTransaction().remove(this).commit()
            //mainView!!.cl_navigation.visibility = View.VISIBLE
            //fm!!.beginTransaction().remove(this).commit()
            //this.onDetach()
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(keyStore: KeyStore) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    ARG_PARAM1 = keyStore
                }
            }
    }
}