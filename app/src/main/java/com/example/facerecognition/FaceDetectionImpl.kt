package com.example.facerecognition

import android.media.Image
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectionImpl {

    private var faceDetectorOption: FaceDetectorOptions = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .build()
    private var detector: FaceDetector = FaceDetection.getClient(faceDetectorOption)


    fun findFace(image: InputImage,listener:(List<Face>)-> Unit) {
        detector
            .process(image)
            .addOnSuccessListener{
                listener.invoke(it)//ini list yang ada wajah
            }
            .addOnFailureListener{
                listener.invoke(listOf())// list pff buat bikin list kosong
            }
    }

}