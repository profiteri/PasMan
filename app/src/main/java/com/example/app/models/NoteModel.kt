package com.example.app.models;

import java.io.Serializable


data class NoteModel(
    val id: Int,
    val titel: ByteArray,
    val text: ByteArray,
    val iv: ByteArray
) : Serializable
