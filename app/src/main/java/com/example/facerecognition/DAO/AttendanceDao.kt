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

    @Query("SELECT * FROM registeredstudent")
    suspend fun getAllStudent(): List<StudentEntity>

    @Query("SELECT * FROM attendant")
    suspend fun getAllAttendance(): List<AttendanceEntity>

    @Query("SELECT * FROM registeredstudent WHERE matric = :matrics ")
    suspend fun getStudentWithId(matrics: String): StudentEntity?

    @Query("SELECT * FROM attendant")
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

    @Query("DELETE FROM attendant WHERE student_matrics = :matric")
    suspend fun deleteAttendanceByMatric(matric: String)

    @Query("DELETE FROM registeredstudent WHERE matric = :matric")
    suspend fun deleteStudentByMatric(matric: String)

    @Query("SELECT * FROM attendant WHERE student_matrics = :matrics")
    suspend fun getAttendanceListByMatrics(matrics: String): List<AttendanceWithStudentEntity>



    @Query("UPDATE registeredstudent SET name = :name WHERE matric = :matric")
    suspend fun updateStudentName(matric: String, name: String)

    @Insert
    suspend fun insertAttendance(attendance: AttendanceEntity)

    @Query("SELECT * FROM attendant WHERE student_matrics = :studentMatrics")
    suspend fun getAttendantListByMatrics(studentMatrics: String): List<AttendanceEntity>

}
