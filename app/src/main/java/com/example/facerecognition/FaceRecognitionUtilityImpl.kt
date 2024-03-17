package com.example.facerecognition

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.nio.ByteBuffer


class FaceRecognitionUtilityImpl (private val context: Context) {



    private var modelinfo : ModelInfo = ModelInfo(
        "FaceNet" ,
        "facenet.tflite" ,
        0.75f ,// semakin dekat dengan 1 maka semakin mirp
        8f ,// semakin kecil semakin baik 0-100
        128 ,
        160
    )
    private var imageTensorProcessor = ImageProcessor.Builder()
        .add(ResizeOp(modelinfo.inputDims,modelinfo.inputDims,ResizeOp.ResizeMethod.BILINEAR))
        .add(StandardizeOp())
        .build()

    private val interpreterOption = Interpreter.Options().apply {


        setNumThreads(4)
        setUseXNNPACK(true)
        setUseNNAPI(true)

    }



    private lateinit var interpreter: Interpreter

     fun init(){
         Log.w("rayhan", "init CALLED")
        val model = FileUtil.loadMappedFile(context,modelinfo.assetsFilename)
        interpreter = Interpreter(model, interpreterOption)
    }

    private fun convertBitmapToBuffer(image: Bitmap): ByteBuffer{
        return imageTensorProcessor.process(TensorImage.fromBitmap(image)).buffer
    }

    fun getFaceEmbedding(bitmap: Bitmap): Array<FloatArray>{
       if (!::interpreter.isInitialized) return arrayOf()
        val t1 = System.currentTimeMillis()
        val faceNetModelOutputs = Array (1){ FloatArray(modelinfo.outputDims) }
        val input = convertBitmapToBuffer(bitmap)
        interpreter.run(input, faceNetModelOutputs)
        Log.i("peformance","${modelinfo.name} Inference Speed in ms : ${System.currentTimeMillis()-t1} ")
        return faceNetModelOutputs
    }

}