package com.example.app.database

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.app.models.CardModel
import com.example.app.models.ProfileModel

class DatabaseProfile(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ProfilesDatabase"
        private const val TABLE_CONTACTS = "ProfilesTable"

        private const val KEY_ID = "_id"
        private const val KEY_SOURCE = "source"
        private const val KEY_LOGIN = "login"
        private const val KEY_PASSWORD = "password"
        private const val KEY_INFO = "info"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create = ("CREATE TABLE " + TABLE_CONTACTS +
                "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SOURCE +
                " TEXT," + KEY_LOGIN + " TEXT," + KEY_PASSWORD + " TEXT," +
                KEY_INFO + " TEXT" + ")")
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addProfile(profile: ProfileModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_SOURCE, profile.source)
        contentValues.put(KEY_LOGIN, profile.login)
        contentValues.put(KEY_PASSWORD, profile.password)
        contentValues.put(KEY_INFO, profile.info)

        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }

    fun viewProfile(): ArrayList<ProfileModel> {
        val profile = ArrayList<ProfileModel>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var source: String
        var login: String
        var password: String
        var info: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                source = cursor.getString(cursor.getColumnIndex(KEY_SOURCE))
                login = cursor.getString(cursor.getColumnIndex(KEY_LOGIN))
                password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))
                info = cursor.getString(cursor.getColumnIndex(KEY_INFO))

                profile.add(ProfileModel(id, source, login, password, info))
            } while (cursor.moveToNext())
        }
        return profile
    }

    fun updateProfile(profile: ProfileModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_SOURCE, profile.source)
        contentValues.put(KEY_LOGIN, profile.login)
        contentValues.put(KEY_PASSWORD, profile.password)
        contentValues.put(KEY_INFO, profile.info)

        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + profile.id, null)
        db.close()
        return success
    }

    fun deleteProfile(profile: ProfileModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, profile.id)
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + profile.id, null)
        db.close()
        return success
    }
}