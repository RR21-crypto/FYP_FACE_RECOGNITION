package com.example.facerecognition

import android.util.Log
import kotlin.math.pow
import kotlin.math.sqrt

object FaceRecognitionUtils {
    fun computeWithL2Norm(x1 : FloatArray, x2 : FloatArray): Float {
        return sqrt(x1.mapIndexed{ i, xi -> (xi - x2[i]).pow(2) }.sum())
    }

    fun computeWithCosineSimilarity(x1 : FloatArray, x2: FloatArray): Float {
        val mag1 = sqrt(x1.map { it * it }.sum())
        val mag2 = sqrt(x2.map { it * it }.sum())
        val dot = x1.mapIndexed{ i, xi -> xi * x2[i] }.sum()
        return dot / (mag1 * mag2)
    }

    fun calculateScore(metricToBeUsed: String = "l2", subject: FloatArray,  faceList: List<Pair<String, FloatArray>>): HashMap<String, ArrayList<Float>> {
        val nameScoreHashmap = HashMap<String, ArrayList<Float>>()

        for (i in faceList.indices) {
            if (nameScoreHashmap[faceList[i].first] == null) {
                val scores = ArrayList<Float>()
                if (metricToBeUsed == "cosine") {
                    scores.add(computeWithCosineSimilarity(subject, faceList[i].second))
                }
                else {
                    scores.add(computeWithL2Norm(subject, faceList[i].second))
                }
                nameScoreHashmap[faceList[i].first] = scores
            } else {
                if (metricToBeUsed == "cosine") {
                    nameScoreHashmap[faceList[i].first]?.add(computeWithCosineSimilarity(subject, faceList[i].second))
                }
                else {
                    nameScoreHashmap[faceList[i].first]?.add(computeWithL2Norm(subject, faceList[i].second))
                }
            }
        }
        return nameScoreHashmap
    }

    fun getPersonNameFromAverageScore( // fungsi / pabrik dimana temoat compare skor , dan disini juga tempat nentuin matrik cosine atau l2 yang digunakan
        metricToBeUsed:String,
        nameScoreHashmap: HashMap<String, ArrayList<Float>>,
        cosineThreshold: Float,
        l2Threshold: Float
    ): Pair<String, Double> {
        val avgScores = nameScoreHashmap.values.map { scores -> scores.toFloatArray().average() }
        Log.i("RayhanFacerecognition", "[$metricToBeUsed] Average score for each user : $nameScoreHashmap")

        val names = nameScoreHashmap.keys.toTypedArray()
        nameScoreHashmap.clear()
        val bestAvgScore: Double
        val bestScoreUserName: String = if (metricToBeUsed == "cosine") {
            bestAvgScore = avgScores.maxOrNull() ?: 0.0
            if (bestAvgScore > cosineThreshold) {
                names[avgScores.indexOf(bestAvgScore)]
            }
            else {
                "Unknown"
            }
        } else {
            bestAvgScore = avgScores.minOrNull() ?: 0.0
            if (bestAvgScore > l2Threshold ) {
                "Unknown"
            }
            else {
                names[avgScores.indexOf(bestAvgScore)]
            }
        }
        return Pair(bestScoreUserName, bestAvgScore)
    }

    fun sayaganteng(){

    }


}