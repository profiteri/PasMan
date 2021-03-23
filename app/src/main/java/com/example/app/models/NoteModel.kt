package com.example.app.models;

import java.io.Serializable


data class NoteModel(
    val id: Int,
    val titel: String,
    val text: String
) : Serializable
