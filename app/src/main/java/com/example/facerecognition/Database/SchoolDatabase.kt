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

@Database(entities = [AttendanceEntity::class, StudentEntity::class], version = 3)
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
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration()  // Optional: Use this if you want to recreate database
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Contoh migrasi dari versi 1 ke versi 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE attendant ADD COLUMN type TEXT NOT NULL DEFAULT 'IN'")
            }
        }

        // Contoh migrasi dari versi 2 ke versi 3
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Tambahkan migrasi yang diperlukan
            }
        }
    }
}
