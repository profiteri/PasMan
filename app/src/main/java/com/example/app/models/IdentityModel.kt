package com.example.app.models;

import java.io.Serializable


data class IdentityModel(
    val id: Int,
    val name: String,
    val surname: String,
    val street: String,
    val app: String,
    val contry: String,
    val postcode: String,
    val phoneNumber: String,
    val email: String
) : Serializable
