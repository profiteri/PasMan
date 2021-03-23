package com.example.app.models

data class CardModel (
    val id: Int,
    val number: String,
    val holder: String,
    val expiry: String,
    val cvc: Int,
    val pin: Int,
    val comment: String
)