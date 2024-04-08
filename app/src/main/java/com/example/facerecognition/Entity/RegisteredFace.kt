package com.example.facerecognition.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "registeredstudent")
data class RegisteredFace(

    @ColumnInfo(name = "name")
    val name : String,
    @ColumnInfo(name = "embedding")
    val embedding : String,
    @ColumnInfo(name ="date")
    val date : String,
    @PrimaryKey
    @ColumnInfo(name = "matric")
    val matric : String,
)
