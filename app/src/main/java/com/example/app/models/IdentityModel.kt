package com.example.app.models;

import java.io.Serializable


data class IdentityModel(
    val id: Int,
    val name: ByteArray,
    val surname: ByteArray,
    val street: ByteArray,
    val app: ByteArray,
    val country: ByteArray,
    val postcode: ByteArray,
    val phoneNumber: ByteArray,
    val email: ByteArray,
    val iv: ByteArray
) : Serializable
