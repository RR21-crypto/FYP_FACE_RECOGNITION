package com.example.facerecognition.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.facerecognition.Entity.AttendanceEntity
import com.example.facerecognition.Entity.AttendanceWithStudentEntity
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
    suspend fun getAllAttendanceWithStudent(): List<AttendanceWithStudentEntity>

    @Query("DELETE FROM attendant")
    suspend fun deleteAllAttendance()

    @Delete
    suspend fun deleteAttendance(attendant: AttendanceEntity)

    @Query("SELECT * FROM attendant WHERE student_matrics = :name")
    suspend fun getAttendanceByName(name: String): AttendanceEntity?

    @Delete
    suspend fun deleteAttendanceByName(attendanceEntity: AttendanceEntity)

    @Delete
    suspend fun deleteRegisterByName(studentEntity: StudentEntity)


}