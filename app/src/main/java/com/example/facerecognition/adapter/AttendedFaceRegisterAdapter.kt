package com.example.facerecognition.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.facerecognition.Entity.AttendanceEntity
import com.example.facerecognition.Entity.AttendanceWithStudentEntity
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.Helper.RoomHelper


import com.example.facerecognition.databinding.LayoutAttendantBinding
import com.example.facerecognition.databinding.LayoutUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class AttendedFaceRegisterAdapter(private val roomHelper: RoomHelper, private val listStudent : List<AttendanceWithStudentEntity>, private val context: Context): RecyclerView.Adapter<AttendedFaceRegisterAdapter.ListViewHolder>(){

    private  val faceRecognitionHelper = FaceRecognitionHelper()


    class ListViewHolder(private val binding:LayoutAttendantBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setview(attendance: AttendanceWithStudentEntity, dateFormat: SimpleDateFormat){
            binding.attendUsernameTextView.text = attendance.studentEntity.name
            val formatDate = dateFormat.format(attendance.attendanceEntity.getAttendanceDateAsDate())
            binding.attendRegisterDateTextView.text = formatDate
            binding.attendMatricNumberTextView.text = attendance.attendanceEntity.studentMatrics
        }
        val tvdelete : ImageButton = binding.attendDeleteButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutAttendantBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return listStudent.size;
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val face  =  listStudent[position]
        holder.setview(face, SimpleDateFormat("dd-MM-yyyy | HH:mm", Locale.getDefault()))

        holder.tvdelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val success = roomHelper.specificDelete(context, name = face.attendanceEntity.studentMatrics)
                if (success) {
                    withContext(Dispatchers.Main) {
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, listStudent.size)
                    }
                } else {
                    // Show an error message
                    return@launch
                }
            }
        }
    }
}