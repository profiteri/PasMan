package com.example.app.crypto

import kotlinx.android.synthetic.main.activity_profile.*
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

class Encrypter(alias : String, customIv : ByteArray?) {
    private val cipher : Cipher = Cipher.getInstance("AES/GCM/NoPadding")
    init {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        if (customIv == null)
            cipher.init(Cipher.ENCRYPT_MODE, keyStore.getKey(alias, null))
        else
            cipher.init(Cipher.ENCRYPT_MODE, keyStore.getKey(alias, null), GCMParameterSpec(128, customIv))
    }

    fun encryptString(string : String) : ByteArray {
        return cipher.doFinal(string.toByteArray(Charsets.UTF_8))
    }

    fun getIv() : ByteArray {
        return cipher.iv
    }
}