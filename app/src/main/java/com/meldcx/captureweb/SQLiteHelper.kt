package com.meldcx.captureweb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by Lakshmi Srividya on 11/26/2019.
 */
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        Log.d("table", CREATE_TABLE_IMAGES)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_IMAGES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS '$TABLE_IMAGES'")
        onCreate(db)
    }

    fun getAllImage(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_IMAGES", null)
    }

    fun addImageDetail(imageUrl: String, date: String, bitmap: String): Long {
        val db = this.writableDatabase
        // Creating content values
        val values = ContentValues()
        values.put(KEY_URL, imageUrl)
        values.put(KEY_DATE, date)
        values.put(KEY_BITMAP, bitmap)

        // insert row in images table
        return db.insert(TABLE_IMAGES, null, values)
    }

    companion object {
        var DATABASE_NAME = "image_database"
        private val DATABASE_VERSION = 1
        private val TABLE_IMAGES = "images"
        val KEY_ID = "id"
        val KEY_URL = "url"
        val KEY_DATE = "date"
        val KEY_BITMAP = "bitmap"

        /*CREATE TABLE images ( id INTEGER PRIMARY KEY AUTOINCREMENT, url TEXT, date TEXT......);*/
        private val CREATE_TABLE_IMAGES = ("CREATE TABLE "
                + TABLE_IMAGES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_URL + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_BITMAP + " TEXT);")
    }
}