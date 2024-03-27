package com.example.facerecognition.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.R
import com.example.facerecognition.RegisteredFace
import com.example.facerecognition.StorageHelper
import com.example.facerecognition.databinding.LayoutUserBinding
import java.util.jar.Attributes.Name


class RegisteredFaceAdapter (private val listStudent : List<RegisteredFace>,private val storageHelper: StorageHelper,private val context: Context): RecyclerView.Adapter<RegisteredFaceAdapter.ListViewHolder>() {

    private  val faceRecognitionHelper = FaceRecognitionHelper()

    class ListViewHolder(private val binding: LayoutUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(registeredFace: RegisteredFace) {
            binding.usernameTextView.text = registeredFace.name
            binding.registerDateTextView.text = registeredFace.date
        }

        val tvdelete : ImageButton = binding.deleteButton

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listStudent.size;
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val face  =  listStudent[position]
        holder.setData(face)
        holder.tvdelete.setOnClickListener{
           storageHelper.specificDelete(context,face.name)
            notifyItemRemoved(position)
        }

    }

}