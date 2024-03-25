package com.example.facerecognition



data class RegisteredFace(
    val name : String,
    val embedding : FloatArray,
    val date : String
)
