package com.example.facerecognition.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "registeredstudent")

data class StudentEntity (
    @ColumnInfo(name = "name")
    val name : String,
    @ColumnInfo(name = "embedding")
    val embedding : String,
    @ColumnInfo(name ="date")
    val date : Long,
    @PrimaryKey
    @ColumnInfo(name = "matric")
    val matric : String,
    @ColumnInfo(name = "image_uri")
    val imageUri : String?,
)
