package com.example.facerecognition.Helper

import android.content.Context
import com.example.facerecognition.DAO.AttendanceDao
import com.example.facerecognition.Database.StudentDatabase
import com.example.facerecognition.Entity.AttendanceEntity
import com.example.facerecognition.Entity.AttendanceWithStudentEntity
import com.example.facerecognition.Entity.StudentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RoomHelper {

    private lateinit var database: StudentDatabase
    private lateinit var attendanceDao: AttendanceDao

    fun init(context: Context) {
        database = StudentDatabase.getDatabase(context)
        attendanceDao = database.attendanceDao()
    }

    suspend fun insertStudent(studentEntity: StudentEntity) {
        database.attendanceDao().insertStudent(studentEntity)
    }

    suspend fun inserrAttendance(attendanceEntity: AttendanceEntity): Boolean {
        val studentExist = database.attendanceDao().getStudentWithId(attendanceEntity.studentMatrics)
        return if (studentExist != null) {
            database.attendanceDao().insertAttendant(attendanceEntity)
            true
        } else {
            false
        }
    }

    suspend fun getALLStudentList(): List<StudentEntity> {
        return database.attendanceDao().getAllStudent()
    }

    suspend fun getAttendantList(): List<AttendanceWithStudentEntity> {
        return database.attendanceDao().getAllAttendanceWithStudent()
    }

    suspend fun specificDelete(context: Context, matrics: String): Boolean {
        val database = StudentDatabase.getDatabase(context)
        database.attendanceDao().deleteStudentByMatric(matrics)
        return true
    }

    suspend fun spesificRegisterDelete(context: Context, matrics: String): Boolean {
        val database = StudentDatabase.getDatabase(context)
        val studentEntity = database.attendanceDao().getStudentWithId(matrics)
        if (studentEntity != null) {
            database.attendanceDao().deleteRegisterByName(studentEntity)
        }
        return true
    }

    suspend fun clearAllAttendance(context: Context) {
        withContext(Dispatchers.IO) {
            attendanceDao.deleteAllAttendance()
        }
    }

    fun convertMillisToDateTime(millis: Long): String {
        val date = Date(millis)
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }

    fun convertDate(millis: Long): String {
        val date = Date(millis)
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun convertHour(millis: Long): String {
        val hour = Date(millis)
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return formatter.format(hour)
    }

    suspend fun deleteRegister(matrics: String) {
        withContext(Dispatchers.IO) {
            attendanceDao.deleteAttendanceByMatric(matrics)
        }
    }

    suspend fun spesificAttendanceDelete(context: Context, matrics: String) {
        withContext(Dispatchers.IO) {
            val studentEntity = database.attendanceDao().getAttendanceByName(matrics)
            if (studentEntity != null) {
                StudentDatabase.getDatabase(context).attendanceDao().deleteAttendanceByName(studentEntity)
            }
        }
    }

    suspend fun getAttendantListByMatrics(matrics: String): List<AttendanceEntity> {
        return database.attendanceDao().getAttendantListByMatrics(matrics)
    }

    suspend fun updateStudentName(matric: String, name: String) {
        attendanceDao.updateStudentName(matric, name)
    }

    suspend fun insertAttendance(attendance: AttendanceEntity) {
        database.attendanceDao().insertAttendance(attendance)
    }


}
