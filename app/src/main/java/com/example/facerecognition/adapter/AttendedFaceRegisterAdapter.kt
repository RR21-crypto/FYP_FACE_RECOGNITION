package com.example.facerecognition.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.RegisteredFace
import com.example.facerecognition.StorageHelper
import com.example.facerecognition.databinding.LayoutAttendantBinding
import com.example.facerecognition.databinding.LayoutUserBinding

class AttendedFaceRegisterAdapter(private val storageHelper: StorageHelper,private val listStudent : List<RegisteredFace>,private val context: Context): RecyclerView.Adapter<AttendedFaceRegisterAdapter.ListViewHolder>(){

    private  val faceRecognitionHelper = FaceRecognitionHelper()

    class ListViewHolder(private val binding:LayoutAttendantBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setview(registeredFace: RegisteredFace){
            binding.attendUsernameTextView.text = registeredFace.name
            binding.attendRegisterDateTextView.text = registeredFace.date
            binding.attendMatricNumberTextView.text = registeredFace.matric

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
        holder.setview(face)
        holder.tvdelete.setOnClickListener{
            storageHelper.specificDelete(context,face.name)
            notifyItemRemoved(position)
        }
    }
}