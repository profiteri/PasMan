package com.example.app.crypto

import kotlinx.android.synthetic.main.activity_profile.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

class Encrypter(secretKey : SecretKey, private val customIv : ByteArray?) {
    private val cipher : Cipher = Cipher.getInstance("AES/GCM/NoPadding")
    init {
        if (customIv == null)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        else
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(8*customIv.size, customIv))
    }

    fun encryptString(string : String) : ByteArray {
        return cipher.doFinal(string.toByteArray(Charsets.UTF_8))
    }

    fun getIv() : ByteArray {
        return cipher.iv
    }
}