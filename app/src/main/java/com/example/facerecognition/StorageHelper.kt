//package com.example.facerecognition
//
//import android.content.Context
//import android.util.Log
//import java.text.SimpleDateFormat
//import java.util.Date
//import com.example.facerecognition.Entity.RegisteredFace
//
//class StorageHelper {
//
//    private val dateFormat = SimpleDateFormat("yyyy MM dd HH:mm")
//
//    fun registerFace(context: Context,name :String, face:FloatArray,matric :String){
//        val sharedPreference = context.getSharedPreferences("Storage_Rayhan", Context.MODE_PRIVATE)
//        val editor = sharedPreference.edit()
//        editor.putString(name, face.joinToString("|"))
//        editor.putString("${name}_date", dateFormat.format(Date()))
//        editor.putString("${name}_matric", matric)
//
////        {
////            "rey": "10f|99f|...",
////            "fikri": ".....",
////            "name": "rey|fikri"
////        }
//
//        var allName = sharedPreference.getString("name", "") ?: ""
//        allName += name + "|"
//        editor.putString("name", allName)
//        editor.commit()
//
//        Log.w("RAY2", "register invoked ${allName} == | ${name} | ${face.joinToString("|")}")
//    }
//
//    fun getRegisterFace(context: Context): List<RegisteredFace> {
//        val sharedPreference = context.getSharedPreferences("Storage_Rayhan", Context.MODE_PRIVATE)
//        val allName = sharedPreference.getString("name", "") ?: ""
//        val finalList = mutableListOf<RegisteredFace>()
//        Log.w("RAY2", "register init ${allName}")
//
//        allName.split("|").forEach {
//            if (it.isNotEmpty()) {
//                val name = it
//                val face = sharedPreference.getString(name, "") ?: ""
//                Log.w("RAY2", "register face ${face}")
//
//                val faceFloatArray = face.split("|").map { it.toFloat() }.toFloatArray()
//                val date = sharedPreference.getString("${name}_date", "Unknown Date") ?: "Unknown Date"
//                val matric = sharedPreference.getString("${name}_matric", "") ?: "" // Define the matric variable before using it
//                finalList.add(RegisteredFace(name, faceFloatArray.joinToString { ";" }, date = date, matric = matric))
//            }
//        }
//        return finalList
//    }
//    fun clearFace(context: Context){
//        val sharedPreference = context.getSharedPreferences("Storage_Rayhan",Context.MODE_PRIVATE).edit()
//        sharedPreference.clear()
//        sharedPreference.apply()
//
//    }
//
//    fun specificDelete(context: Context, name: String) {
//        val sharedPreference = context.getSharedPreferences("Storage_Rayhan", Context.MODE_PRIVATE)
//
//        val allName = sharedPreference.getString("name", "") ?: ""
//
//        if (allName.isNotEmpty()) {
//            val names = allName.split("|")
//            val updatedNames = names.filterNot { it == name }
//            val updatedNameString = updatedNames.joinToString("|")
//
//            sharedPreference.edit().apply {
//                putString("name", updatedNameString)
//                remove(name)
//                remove("${name}_date")
//                remove("${name}_matric") // Remove the matric number as well
//                apply()
//            }
//        }
//    }
//}