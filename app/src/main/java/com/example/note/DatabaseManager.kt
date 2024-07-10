package com.example.note

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.google.firebase.Timestamp

class DatabaseManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)
    private val db:SQLiteDatabase = dbHelper.writableDatabase

    fun addUser(name:String,email: String):Long{
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_USER_NAME,name)
            put(DatabaseHelper.COLUMN_USER_EMAIL,email)
        }
        return db.insert(DatabaseHelper.TABLE_USER,null,values)
    }

    fun getUserIdByEmail(email: String): Long? {
        val cursor = db.query(
            DatabaseHelper.TABLE_USER,
            arrayOf(DatabaseHelper.COLUMN_USER_ID),
            "${DatabaseHelper.COLUMN_USER_EMAIL} = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return it.getLong(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID))
            }
        }
        return null
    }
    fun getUser(userId: Long): Cursor {
        return db.query(DatabaseHelper.TABLE_USER, null, "${DatabaseHelper.COLUMN_USER_ID} = ?", arrayOf(userId.toString()), null, null, null)
    }

    fun getAllUsers(): Cursor {
        return db.query(DatabaseHelper.TABLE_USER, null, null, null, null, null, null)
    }

    fun updateUser(userId: Long, name: String, email: String): Int {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_USER_NAME, name)

        }
        return db.update(DatabaseHelper.TABLE_USER, values, "${DatabaseHelper.COLUMN_USER_ID} = ?", arrayOf(userId.toString()))
    }

    fun deleteUser(userId: Long): Int {
        return db.delete(DatabaseHelper.TABLE_USER, "${DatabaseHelper.COLUMN_USER_ID} = ?", arrayOf(userId.toString()))
    }


    fun addNote(title: String, content: String, userId: Long,timestamp: String): Long {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NOTE_TITLE, title)
            put(DatabaseHelper.COLUMN_NOTE_DESCRIPTION, content)
            put(DatabaseHelper.COLUMN_USER_ID_FK, userId)
            put(DatabaseHelper.COLUMN_TIMESTAMP,timestamp)

        }
        val result = db.insert(DatabaseHelper.TABLE_NOTES, null, values)
        Log.d("DatabaseManager", "Note added with result: $result")
        return result
    }

    fun getNote(noteId: Long): Cursor {
        return db.query(DatabaseHelper.TABLE_NOTES, null, "${DatabaseHelper.COLUMN_NOTE_ID} = ?", arrayOf(noteId.toString()), null, null, null)
    }

    fun getAllNotesByUser(userId: Long): Cursor {
        val cursor = db.query(
            DatabaseHelper.TABLE_NOTES,
            null,
            "${DatabaseHelper.COLUMN_USER_ID_FK} = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )
        Log.d("DatabaseManager", "getAllNotesByUser cursor count: ${cursor.count}")
        return cursor
    }


    fun updateNote(noteId: Double, title: String, content: String,timestamp: String): Int {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NOTE_TITLE, title)
            put(DatabaseHelper.COLUMN_NOTE_DESCRIPTION, content)
            put(DatabaseHelper.COLUMN_TIMESTAMP,timestamp)
        }
        return db.update(DatabaseHelper.TABLE_NOTES, values, "${DatabaseHelper.COLUMN_NOTE_ID} = ?", arrayOf(noteId.toString()))
    }

    fun deleteNote(noteId: Double): Int {
        val rowsDeleted = db.delete(DatabaseHelper.TABLE_NOTES, "${DatabaseHelper.COLUMN_NOTE_ID} = ?", arrayOf(noteId.toString()))
        Log.d("DatabaseManager", "Note ID $noteId deleted, rows affected: $rowsDeleted")
        return rowsDeleted
    }






















}