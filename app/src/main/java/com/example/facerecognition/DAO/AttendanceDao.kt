package com.example.facerecognition.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.facerecognition.Entity.AttendanceEntity
import com.example.facerecognition.Entity.AttendanceWithStudent
import com.example.facerecognition.Entity.StudentEntity

@Dao

interface AttendanceDao {

    @Insert
    suspend fun insertStudent(registeredStudent: StudentEntity)

    @Insert
    suspend fun insertAttendant(attendant: AttendanceEntity)

    @Query("SELECT*FROM registeredstudent")
    suspend fun getAllStudent(): List<StudentEntity>

    @Query("SELECT*FROM attendant")
    suspend fun getAllAttendance(): List<AttendanceEntity>

//    @Query("SELECT * FROM registeredstudent WHERE matrics NOT IN (SELECT matrics FROM attendant)")
//    suspend fun getAllAttendanceWithStudent(): List<StudentEntity>

    @Query("SELECT * FROM registeredstudent WHERE matric = :matrics ")
    suspend fun getStudentWithId(matrics :String):StudentEntity?

    @Query("SELECT*FROM attendant")
    suspend fun getAllAttendanceWithStudent(): List<AttendanceWithStudent>


}