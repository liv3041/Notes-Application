package com.example.note

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Random
import java.util.UUID

class UserViewModel:ViewModel() {
    private lateinit var  databaseRef : DatabaseReference
    private lateinit var noteDatabaseReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var uuid:String
    private val TAG = "UserViewModel"

    init {
        saveUserData()



    }

    fun saveUserData() {
        val database = FirebaseDatabase.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            uuid = firebaseUser.uid
            val username = firebaseUser.displayName
            val user = User(uuid = uuid, userName = username)
            databaseRef = database.getReference("users").child(uuid)

            databaseRef.setValue(user)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "User data saved successfully")
                    } else {
                        Log.e(TAG, "Failed to save user data: ${it.exception}")
                    }
                }

//            val note = Note(title = title, description = description,timestamp=timestamp,noteID)
//            databaseRef.setValue(note).addOnCompleteListener {
//                if(it.isSuccessful){
//                    Log.d(TAG, "User data saved successfully")
//                }else{
//                    Log.e(TAG, "Failed to save user data: ${it.exception}")
//                }
//            }

        }
    }

//        fun saveNoteData(title:String,description:String,timestamp:String,noteID: Double){
//
//            val database = FirebaseDatabase.getInstance()
//
//
//            val firebaseUser = FirebaseAuth.getInstance().currentUser
//
//            if (firebaseUser != null) {
//                uuid = firebaseUser.uid
//                val noteId = generateUniqueNoteId()
//                val note =
//                    Note(title = title, description = description, timestamp = timestamp, noteID)
//
//
//                noteDatabaseReference = database.getReference("notes").child(uuid).child(noteId)
//
//
//
//
//
//                noteDatabaseReference.setValue(note)
//                    .addOnCompleteListener { noteTask ->
//                        if (noteTask.isSuccessful) {
//                            Log.d(TAG, "Note data saved successfully")
//                        } else {
//                            Log.e(TAG, "Failed to save note data: ${noteTask.exception}")
//                        }
//                    }
//
//            }
//        }




    }

    fun generateUniqueNoteId(): String {
        return UUID.randomUUID().toString()
    }

