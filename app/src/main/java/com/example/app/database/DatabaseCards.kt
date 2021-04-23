package com.example.app.database

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.app.models.CardModel

class DatabaseCards(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CardsDatabase"
        private const val TABLE_CONTACTS = "CardsTable"

        private const val KEY_ID = "_id"
        private const val KEY_NUMBER = "number"
        private const val KEY_HOLDER = "holder"
        private const val KEY_EXPIRY = "expiry"
        private const val KEY_CVC = "cvc"
        private const val KEY_PIN = "pin"
        private const val KEY_COMMENT = "comment"
        private const val KEY_IV = "iv"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create = ("CREATE TABLE " + TABLE_CONTACTS +
                "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMBER +
                " BLOB," + KEY_HOLDER + " BLOB," + KEY_EXPIRY + " BLOB," +
                KEY_CVC + " BLOB," + KEY_PIN + " BLOB," +
                KEY_COMMENT + " BLOB," + KEY_IV + " BLOB" + ")")
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addCard(card: CardModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NUMBER, card.number)
        contentValues.put(KEY_HOLDER, card.holder)
        contentValues.put(KEY_EXPIRY, card.expiry)
        contentValues.put(KEY_CVC, card.cvc)
        contentValues.put(KEY_PIN, card.pin)
        contentValues.put(KEY_COMMENT, card.comment)
        contentValues.put(KEY_IV, card.iv)

        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }

    fun viewCards(): ArrayList<CardModel> {
        val card = ArrayList<CardModel>()
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
        var number: ByteArray
        var holder: ByteArray
        var expiry: ByteArray
        var cvc: ByteArray
        var pin: ByteArray
        var comment: ByteArray
        var iv: ByteArray

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                number = cursor.getBlob(cursor.getColumnIndex(KEY_NUMBER))
                holder = cursor.getBlob(cursor.getColumnIndex(KEY_HOLDER))
                expiry = cursor.getBlob(cursor.getColumnIndex(KEY_EXPIRY))
                cvc = cursor.getBlob(cursor.getColumnIndex(KEY_CVC))
                pin = cursor.getBlob(cursor.getColumnIndex(KEY_PIN))
                comment = cursor.getBlob(cursor.getColumnIndex(KEY_COMMENT))
                iv = cursor.getBlob(cursor.getColumnIndex(KEY_IV))

                card.add(CardModel(id, number, holder, expiry, cvc, pin, comment, iv))
            } while (cursor.moveToNext())
        }
        return card
    }

    fun updateCard(card: CardModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NUMBER, card.number)
        contentValues.put(KEY_HOLDER, card.holder)
        contentValues.put(KEY_EXPIRY, card.expiry)
        contentValues.put(KEY_CVC, card.cvc)
        contentValues.put(KEY_PIN, card.pin)
        contentValues.put(KEY_COMMENT, card.comment)
        contentValues.put(KEY_IV, card.iv)

        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + card.id, null)
        db.close()
        return success
    }

    fun deleteCard(card: CardModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, card.id)
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + card.id, null)
        db.close()
        return success
    }
}