package com.example.facerecognition.Entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class RegisteredFace(
    val name : String,
    val embedding : FloatArray,
    val date : Long,
    val matric : String,
    val imageUri: String? = null
) : Parcelable {
    fun getDateAsDate(): Date {
        return Date(date)
    }
}
