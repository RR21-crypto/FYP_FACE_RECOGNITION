package com.example.facerecognition.Entity

import androidx.room.Embedded
import androidx.room.Relation

data class AttendanceWithStudent(
    @Embedded
    val attendanceEntity: AttendanceEntity,
    @Relation(
        parentColumn = "student_matrics",
        entityColumn = "matric"
    )
    val studentEntity: StudentEntity?
)