package com.example.app.crypto

import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

class Decrypter(iv: ByteArray) {
    private val cipher : Cipher = Cipher.getInstance("AES/GCM/NoPadding")
    private var alias : String
    init {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        this.alias = keyStore.aliases().nextElement()
        cipher.init(Cipher.DECRYPT_MODE, keyStore.getKey(this.alias,null), GCMParameterSpec(128, iv))
    }

    fun decryptString(string: ByteArray) : String {
        return String(cipher.doFinal(string), Charsets.UTF_8)
    }
}