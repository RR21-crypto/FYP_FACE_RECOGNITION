package com.example.facerecognition.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.Helper.RoomHelper

import com.example.facerecognition.databinding.LayoutUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale


class RegisteredFaceAdapter (private val listStudent : List<RegisteredFace>, private val roomHelper: RoomHelper, private val context: Context): RecyclerView.Adapter<RegisteredFaceAdapter.ListViewHolder>() {

    private  val faceRecognitionHelper = FaceRecognitionHelper()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }




    class ListViewHolder(private val binding: LayoutUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(registeredFace: RegisteredFace,dateFormat: SimpleDateFormat) {
            binding.usernameTextView.text = registeredFace.name
            val formatDate = dateFormat.format(registeredFace.getDateAsDate())
            binding.registerDateTextView.text = formatDate
            binding.matricNumber.text = registeredFace.matric

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
        holder.setData(face,SimpleDateFormat("dd-MM-yyyy | HH:mm", Locale.getDefault()))
        holder.tvdelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val success = roomHelper.spesificRegisterDelete(context, matrics = face.matric)
                if (success) {
                    withContext(Dispatchers.Main) {
                        val newItems = ArrayList(listStudent)
                        newItems.removeAt(position)

                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, listStudent.size)


                    }
                } else {
                    // Show an error message
                    return@launch
                }
            }
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listStudent[holder.adapterPosition]) }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: RegisteredFace)
    }



}