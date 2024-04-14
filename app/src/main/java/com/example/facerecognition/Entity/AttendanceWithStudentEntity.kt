package com.example.facerecognition.Entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.facerecognition.Helper.RoomHelper
import java.time.LocalDateTime


data class AttendanceWithStudentEntity (

    @Embedded
    val attendanceEntity: AttendanceEntity,
    @Relation(
        parentColumn = "student_matrics",
        entityColumn = "matric"
    )
    val studentEntity: StudentEntity,
)
