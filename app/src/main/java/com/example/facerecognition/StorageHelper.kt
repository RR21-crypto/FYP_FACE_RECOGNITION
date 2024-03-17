package com.example.facerecognition

import android.content.Context
import android.util.Log

class StorageHelper {

    fun registerFace(context: Context,name :String, face:FloatArray){
        val sharedPreference = context.getSharedPreferences("Storage_Rayhan",Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(name,face.joinToString("|"))

//        {
//            "rey": "10f|99f|...",
//            "fikri": ".....",
//            "name": "rey|fikri"
//        }

        var allName = sharedPreference.getString("name","") ?:""
        allName += name + "|"
        editor.putString("name",allName)
        editor.commit()
        Log.w("RAY2", "register invoked ${allName} == | ${name} | ${face.joinToString("|")}")
    }

    fun getRegisterFace (context: Context): List<Pair<String, FloatArray>> {
        val sharedPreference = context.getSharedPreferences("Storage_Rayhan",Context.MODE_PRIVATE)
        val allName = sharedPreference.getString("name", "") ?: ""
        val finalList = mutableListOf<Pair<String, FloatArray>>()
        Log.w("RAY2", "register init ${allName}")

        allName.split("|").forEach {
            if (it.isNotEmpty()) {
                val name = it
                val face = sharedPreference.getString(name, "") ?: "" // ini bagian nyambungin kata kuci dengan apa yang ida punya
                Log.w("RAY2", "register face ${face}")

                val faceFloatArray = face.split("|").map { it.toFloat() }.toFloatArray()
                finalList.add(Pair(name, faceFloatArray))
            }
        }
        return finalList
    }
    fun clearFace(context: Context){
        val sharedPreference = context.getSharedPreferences("Storage_Rayhan",Context.MODE_PRIVATE).edit()
        sharedPreference.clear()
        sharedPreference.commit()
    }
}