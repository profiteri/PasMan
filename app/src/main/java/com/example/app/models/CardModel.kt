package com.example.app.models

data class CardModel (
    val id: Int,
    val number: ByteArray,
    val holder: ByteArray,
    val expiry: ByteArray,
    val cvc: ByteArray,
    val pin: ByteArray,
    val comment: ByteArray,
    val iv: ByteArray
)