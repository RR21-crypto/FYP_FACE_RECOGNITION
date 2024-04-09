package com.example.facerecognition.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.facerecognition.Entity.AttendanceEntity
import com.example.facerecognition.Entity.AttendanceWithStudentEntity
import com.example.facerecognition.Entity.RegisteredFace


@Dao

interface AttendaceDao {

    @Insert
    suspend fun  InsertStudent(registeredFace: RegisteredFace)

    @Insert
    suspend fun InsertAttendance(attendanceEntity: AttendanceEntity)

    @Query("SELECT*FROM registeredstudent")
    suspend fun getAllStudent(): List<RegisteredFace>

    @Query("SELECT*FROM attendant")
    suspend fun getAllAttendance(): List<AttendanceEntity>

    @Query("SELECT * FROM registeredstudent WHERE matric = :matrics ")
    suspend fun getStudentWithId(matrics :String):RegisteredFace?

    @Query("SELECT*FROM attendant")
    suspend fun getAllAttendanceWithStudent() : List<AttendanceWithStudentEntity>



}