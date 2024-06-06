package com.example.facerecognition.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "attendant")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "student_matrics")
    val studentMatrics: String,
    @ColumnInfo(name = "attendance_date")
    val attendanceDate: Long,
    @ColumnInfo(name = "type")
    val type: String
){
    fun getAttendanceDateAsDate(): Date {
        return Date(attendanceDate)
    }
}
