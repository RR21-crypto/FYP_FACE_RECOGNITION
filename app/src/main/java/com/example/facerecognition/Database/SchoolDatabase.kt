package com.example.facerecognition.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.facerecognition.DAO.AttendanceDao
import com.example.facerecognition.Entity.AttendanceEntity
import com.example.facerecognition.Entity.StudentEntity

@Database(entities = [AttendanceEntity::class, StudentEntity::class], version = 1)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        fun getDatabase(context: Context): StudentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentDatabase::class.java,
                    "student_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }


    }
}