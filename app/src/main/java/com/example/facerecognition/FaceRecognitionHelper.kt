package com.example.facerecognition

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.Entity.StudentEntity
import com.example.facerecognition.Helper.RoomHelper
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class FaceRecognitionHelper {
    private  var isModelReady = false
    private val faceDetectionImpl = FaceDetectionImpl()
    private lateinit var faceRecognitionUtilityImpl: FaceRecognitionUtilityImpl
    private val registeredFace = mutableListOf<RegisteredFace>()

    private val roomHelper = RoomHelper()

   suspend fun init(context: Context){
        faceRecognitionUtilityImpl = FaceRecognitionUtilityImpl(context)
        faceRecognitionUtilityImpl.init()
        isModelReady = true
       roomHelper.init(context)
        registeredFace.addAll(roomHelper.getALLStudentList().map { RegisteredFace(it.name,it.embedding.split(";").map { it.toFloat() }.toFloatArray(),it.date,it.matric, it.imageUri) })
    }

    suspend fun recognizerFace(
        frameBitmap: Bitmap,
        listener : (Pair<RegisteredFace,Double>?)->Unit
    ) {
        if (isModelReady == false){
            listener.invoke(null)
            return
        }
        if (registeredFace.isEmpty()){
            listener.invoke(null)
            return
        }
        val inputImage = InputImage.fromBitmap(frameBitmap,0)
        faceDetectionImpl.findFace(inputImage){
            if (it.isNotEmpty()){
                val face = it.first()
                val cropBitmap = BitmapUtils.cropImageFaceBitmapWithoutResize(frameBitmap,face.boundingBox)
                val faceEmbedding = faceRecognitionUtilityImpl.getFaceEmbedding(cropBitmap) // muka yang mau dideteksi
                val nameScoreHashmap = FaceRecognitionUtils.calculateScore(subject = faceEmbedding[0], faceList = registeredFace) // hasil perbandingan wajah yang di dapat
                var strFinal = """
                                Average score for each user : $nameScoreHashmap
                            """.trimIndent()

                val bestScoreUserName = FaceRecognitionUtils.getPersonNameFromAverageScore( // fun ini m
                    "l2",
                    nameScoreHashmap,
                    0.75f, // ini maksudnya 75 persen
                    8f  // ini maksudnya erornya hanya 8 persen
                )

                Log.w("rayhan",bestScoreUserName.first)
                if (bestScoreUserName.first == "Unknown") {
                    listener.invoke(null)
                } else {
                    listener.invoke(Pair(registeredFace.first { it.matric == bestScoreUserName.first }, bestScoreUserName.second))
                }

            }else{
                Log.w("rayhan","face not found")
                listener.invoke(null)

            }
        }
    }
    suspend fun registerFace(
        context: Context,
        frameBitmap: Bitmap,
        name : String,
        matric : String,
        listener : (Boolean?)->Unit
    ) {
        if (isModelReady == false){
            listener.invoke(false)
            return
        }

        val inputImage = InputImage.fromBitmap(frameBitmap,0)
        faceDetectionImpl.findFace(inputImage){
            if (it.isNotEmpty()){
                val face = it.first()
                val cropBitmap = BitmapUtils.cropImageFaceBitmapWithoutResize(frameBitmap,face.boundingBox)
                val faceEmbedding = faceRecognitionUtilityImpl.getFaceEmbedding(cropBitmap)
                Log.w("rayhan",faceEmbedding[0].size.toString())
                registeredFace.add(RegisteredFace(name,faceEmbedding[0], date = System.currentTimeMillis(), matric = matric))
                runBlocking {
                    roomHelper.insertStudent(StudentEntity(
                        name = name,
                        embedding = faceEmbedding[0].joinToString( ";" ),
                        date = System.currentTimeMillis(),
                        matric = matric,
                        imageUri = saveBitmapToExternalCacheDir("${name}_${matric}", cropBitmap, context)?.toString()
                    ))
                    listener.invoke(true)
                }

            }else{
                Log.w("rayhan","face not found")
                listener.invoke(false)

            }
        }
    }

    fun saveBitmapToExternalCacheDir(studentName: String, bitmap: Bitmap, context: Context): Uri? {
        val externalCacheDir = context.externalCacheDir

        val fileName = "${studentName}.jpg"
        if (externalCacheDir != null) {
            // Create a file in the external cache directory with the specified filename
            val file = File(externalCacheDir, fileName)

            // Try to save the bitmap to the file
            var fileOutputStream: FileOutputStream? = null
            return try {
                fileOutputStream = FileOutputStream(file)
                // Compress and write the bitmap to the output stream
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                Uri.fromFile(file)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } finally {
                // Close the output stream
                try {
                    fileOutputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            return null
        }
    }

    fun clearFace(context: Context){
        registeredFace.clear()
//        roomHelper.clearFace(context)
    }


}