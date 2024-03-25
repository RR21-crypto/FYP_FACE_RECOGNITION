package com.example.facerecognition

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage


class FaceRecognitionHelper {
    private  var isModelReady = false
    private val faceDetectionImpl = FaceDetectionImpl()
    private lateinit var faceRecognitionUtilityImpl: FaceRecognitionUtilityImpl
    private val registeredFace = mutableListOf<Pair<String,FloatArray>>()
    private val storageHelper = StorageHelper()

   fun init(context: Context){
        faceRecognitionUtilityImpl = FaceRecognitionUtilityImpl(context)
        faceRecognitionUtilityImpl.init()
        isModelReady = true
        registeredFace.addAll(storageHelper.getRegisterFace(context))
    }

    suspend fun recognizerFace(
        frameBitmap: Bitmap,
        listener : (Pair<String,Double>?)->Unit
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
                listener.invoke(bestScoreUserName)

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
                registeredFace.add(Pair(name,faceEmbedding[0]))
                storageHelper.registerFace(context, name, faceEmbedding[0])
                listener.invoke(true)
            }else{
                Log.w("rayhan","face not found")
                listener.invoke(false)

            }
        }
    }

    fun clearFace(context: Context){
        registeredFace.clear()
        storageHelper.clearFace(context)
    }


}