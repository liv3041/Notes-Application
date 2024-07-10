package com.example.note

import com.google.firebase.Timestamp
import java.util.Random

data class Note (
    val title:String = "",
    val description:String = "",
    val timestamp: String = "",
    val noteId:Double
)