package com.example.facerecognition.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.facerecognition.DAO.AttendaceDao
import com.example.facerecognition.Entity.AttendanceEntity
import com.example.facerecognition.Entity.RegisteredFace


@Database(entities = [AttendanceEntity::class, RegisteredFace::class], version = 1)
abstract class StudentDatabase:RoomDatabase() {
    abstract fun attendanceDao(): AttendaceDao

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        fun getDatabase(context: Context): StudentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentDatabase::class.java,
                    "student_database"
                ).addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE attendant ADD COLUMN time INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
