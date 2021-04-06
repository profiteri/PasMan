package com.example.app.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.app.models.IdentityModel

class DatabaseIdentity(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "IdentityDatabse" // Database name
        private const val TABLE_IDENTITY = "IdentityTable" // Table Name

        //All the Columns names
        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_SURNAME = "surname"
        private const val KEY_STREET = "street"
        private const val KEY_APP = "appartement"
        private const val KEY_CONUTRY = "contry"
        private const val KEY_POSTCODE = "postcode"
        private const val KEY_PHONE = "phoneNumber"
        private const val KEY_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields

        val CREATE_IDENTITY_TABLE = ("CREATE TABLE " + TABLE_IDENTITY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_SURNAME + " TEXT,"
                + KEY_STREET + " TEXT,"
                + KEY_APP + " TEXT,"
                + KEY_CONUTRY + " TEXT,"
                + KEY_POSTCODE + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT)"


                )
        db?.execSQL(CREATE_IDENTITY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_IDENTITY")
        onCreate(db)
    }

    fun addIdentity(identity: IdentityModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, identity.name) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_SURNAME, identity.surname) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_STREET, identity.street) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_APP, identity.app) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_CONUTRY, identity.contry) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_POSTCODE, identity.postcode) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_PHONE, identity.phoneNumber) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_EMAIL, identity.email) // HappyPlaceModelClass DESCRIPTION


        // Inserting Row
        val result = db.insert(TABLE_IDENTITY, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }

    fun updateIdentity(identity: IdentityModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, identity.name) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_SURNAME, identity.surname) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_STREET, identity.street) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_APP, identity.app) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_CONUTRY, identity.contry) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_POSTCODE, identity.postcode) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_PHONE, identity.phoneNumber) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_EMAIL, identity.email) // HappyPlaceModelClass DESCRIPTION


        // Inserting Row
        val success = db.update(TABLE_IDENTITY, contentValues, KEY_ID + "=" + identity.id, null)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    fun deleteIdentity(note: IdentityModel): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_IDENTITY, KEY_ID + "=" + note.id, null)
        db.close()
        return success
    }

    fun getIdentitiesList(): ArrayList<IdentityModel> {
        val identityList = ArrayList<IdentityModel>()
        val selectQuery = "SELECT * FROM $TABLE_IDENTITY"
        val db = this.readableDatabase
        try {

            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val identity =
                        IdentityModel(
                            cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                            cursor.getString(cursor.getColumnIndex(KEY_SURNAME)),
                            cursor.getString(cursor.getColumnIndex(KEY_STREET)),
                            cursor.getString(cursor.getColumnIndex(KEY_APP)),
                            cursor.getString(cursor.getColumnIndex(KEY_CONUTRY)),
                            cursor.getString(cursor.getColumnIndex(KEY_POSTCODE)),
                            cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                            cursor.getString(cursor.getColumnIndex(KEY_EMAIL))


                        )
                    identityList.add(identity)

                } while (cursor.moveToNext())
            }

            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return identityList

    }
}
