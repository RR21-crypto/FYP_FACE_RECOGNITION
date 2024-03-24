package com.example.facerecognition

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date

class StorageHelper {

    private val dateFormat = SimpleDateFormat("yyyy MM dd HH:mm")

    fun registerFace(context: Context,name :String, face:FloatArray){
        val sharedPreference = context.getSharedPreferences("Storage_Rayhan",Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(name,face.joinToString("|"))
        editor.putString("${name}_date", dateFormat.format(Date()))
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

    fun getRegisterFace (context: Context): List<RegisteredFace> {
        val sharedPreference = context.getSharedPreferences("Storage_Rayhan",Context.MODE_PRIVATE)
        val allName = sharedPreference.getString("name", "") ?: ""
        val finalList = mutableListOf<RegisteredFace>()
        Log.w("RAY2", "register init ${allName}")

        allName.split("|").forEach {
            if (it.isNotEmpty()) {
                val name = it
                val face = sharedPreference.getString(name, "") ?: "" // ini bagian nyambungin kata kuci dengan apa yang ida punya
                Log.w("RAY2", "register face ${face}")

                val faceFloatArray = face.split("|").map { it.toFloat() }.toFloatArray()
                val date = sharedPreference.getString("${name}_date", "Unknown Date") ?: "Unknown Date"
                finalList.add(RegisteredFace(name, faceFloatArray, date = date))
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