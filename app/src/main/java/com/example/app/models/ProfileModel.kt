package com.example.app.models

data class ProfileModel (
    val id: Int,
    val source: ByteArray,
    val login: ByteArray,
    val password: ByteArray,
    val info: ByteArray,
    val iv: ByteArray
)