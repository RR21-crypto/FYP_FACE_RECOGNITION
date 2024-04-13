package com.example.facerecognition.Helper

import android.content.Context
import com.example.facerecognition.Database.StudentDatabase
import com.example.facerecognition.Entity.AttendanceEntity
import com.example.facerecognition.Entity.AttendanceWithStudentEntity
import com.example.facerecognition.Entity.StudentEntity

class RoomHelper {

    private lateinit var database: StudentDatabase

    fun init(context: Context) {
        database = StudentDatabase.getDatabase(context)
    }

    suspend fun insertStudent(studentEntity: StudentEntity) {
        database.attendanceDao().insertStudent(studentEntity)
    }

    suspend fun inserrAttendance(attendanceEntity: AttendanceEntity):Boolean{
        val studentExist = database.attendanceDao().getStudentWithId(attendanceEntity.studentMatrics)

        return if(studentExist != null){
            database.attendanceDao().insertAttendant(attendanceEntity)
            true
        }else{
            false
        }

    }

    suspend fun getALLStudentList(): List<StudentEntity> {
        return database.attendanceDao().getAllStudent()
    }

    suspend fun getAttendantList(): List<AttendanceWithStudentEntity> {
        return database.attendanceDao().getAllAttendanceWithStudent()
    }


}