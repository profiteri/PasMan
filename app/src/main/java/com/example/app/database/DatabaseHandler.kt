package com.example.app.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.example.app.models.NoteModel

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "HappyPlacesDatabase" // Database name
        private const val TABLE_NOTE = "NotesTable" // Table Name

        //All the Columns names
        private const val KEY_TITLE = "title"
        private const val KEY_TEXT = "text"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_HAPPY_PLACE_TABLE = ("CREATE TABLE " + TABLE_NOTE + "("
                + KEY_TITLE + " TEXT,"
                + KEY_TEXT + " TEXT,"
                )
        db?.execSQL(CREATE_HAPPY_PLACE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NOTE")
        onCreate(db)
    }

    fun addHappyPlace(note: NoteModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, note.titel) // HappyPlaceModelClass TITLE
        contentValues.put(
            KEY_TEXT,
            note.text
        ) // HappyPlaceModelClass DESCRIPTION


        // Inserting Row
        val result = db.insert(TABLE_NOTE, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }
}
