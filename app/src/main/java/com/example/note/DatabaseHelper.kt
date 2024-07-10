package com.example.note

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "app_database.db"
        private  const val DATABASE_VERSION = 1

        const val TABLE_USER = "user"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NAME = "name"
        const val COLUMN_USER_EMAIL = "email"


        const val TABLE_NOTES = "notes"
        const val COLUMN_NOTE_ID = "notes_id"
        const val COLUMN_NOTE_TITLE = "title"
        const val COLUMN_NOTE_DESCRIPTION = "content"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_USER_ID_FK = "user_id"

    }

    override fun onCreate(p0: SQLiteDatabase?) {

        val createUserTable = "CREATE TABLE $TABLE_USER(" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "$COLUMN_USER_EMAIL TEXT, " +
                "$COLUMN_USER_NAME TEXT)"


        p0?.execSQL(createUserTable)

        val createNotesTable = "CREATE TABLE $TABLE_NOTES(" +
                "$COLUMN_NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NOTE_TITLE TEXT, " +
                "$COLUMN_NOTE_DESCRIPTION TEXT, "+
                "$COLUMN_TIMESTAMP TEXT, "+
                "$COLUMN_USER_ID_FK INTEGER, "+
                "FOREIGN KEY($COLUMN_USER_ID_FK) REFERENCES $TABLE_USER($COLUMN_USER_ID))"

        p0?.execSQL(createNotesTable)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(p0)

    }
}