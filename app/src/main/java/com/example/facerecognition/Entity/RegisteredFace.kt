package com.example.facerecognition.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



data class RegisteredFace(
    val name : String,
    val embedding : FloatArray,
    val date : Long,
    val matric : String,
)
