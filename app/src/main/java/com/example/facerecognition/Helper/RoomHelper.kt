package com.example.facerecognition.Helper

import android.content.Context
import com.example.facerecognition.Database.StudentDatabase

class RoomHelper {
    private lateinit var database: StudentDatabase

    fun init(context: Context) {
        database = StudentDatabase.getDatabase(context)
    }


}