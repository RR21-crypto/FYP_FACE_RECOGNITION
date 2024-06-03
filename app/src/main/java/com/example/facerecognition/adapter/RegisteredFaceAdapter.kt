package com.example.facerecognition.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.facerecognition.Entity.AttendanceWithStudentEntity
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.Helper.RoomHelper
import com.example.facerecognition.databinding.LayoutUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class RegisteredFaceAdapter(
    private var listStudent: List<RegisteredFace>,
    private val roomHelper: RoomHelper,
    private val context: Context,
    private val attendantList: List<AttendanceWithStudentEntity>
) : RecyclerView.Adapter<RegisteredFaceAdapter.ListViewHolder>() {

    private val faceRecognitionHelper = FaceRecognitionHelper()
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listStudents: MutableList<RegisteredFace> = listStudent.sortedBy { it.name }.toMutableList()
    private val attendantLists: MutableList<AttendanceWithStudentEntity> = attendantList.toMutableList()
    private var setOnDeleteListener: (() -> Unit)? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnDeleteListener(listener: () -> Unit) {
        this.setOnDeleteListener = listener
    }

    class ListViewHolder(private val binding: LayoutUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(registeredFace: RegisteredFace, dateFormat: SimpleDateFormat) {
            binding.usernameTextView.text = registeredFace.name
            val formatDate = dateFormat.format(registeredFace.getDateAsDate())
            binding.registerDateTextView.text = formatDate
            binding.matricNumber.text = registeredFace.matric
            Log.w("RAY", "Uri: ${registeredFace.imageUri}")
            registeredFace.imageUri?.let {
                Uri.parse(it)?.let { uri ->
                    Log.w("RAY", "Uri: ${uri}")
                    Glide.with(binding.root.context).load(uri).into(binding.avatarImageView)
                }
            }

        }

        val tvdelete: ImageButton = binding.deleteButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listStudents.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val face = listStudents[position]
        holder.setData(face, SimpleDateFormat("dd-MM-yyyy | HH:mm", Locale.getDefault()))
        holder.tvdelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                roomHelper.specificDelete(context, matrics = face.matric)
                roomHelper.deleteRegister(matrics = face.matric)
                withContext(Dispatchers.Main) {
                    val indexInAttendantList = attendantLists.indexOfFirst { it.attendanceEntity.studentMatrics == face.matric }
                    if (indexInAttendantList != -1) {
                        attendantLists.removeAt(indexInAttendantList)
                    }
                    listStudents.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, listStudents.size)
                    setOnDeleteListener?.invoke()
                }
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listStudents[holder.adapterPosition])
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: RegisteredFace)
    }

    fun updateList(newList: List<RegisteredFace>) {
        listStudents.clear()
        listStudents.addAll(newList.sortedBy { it.name }) // Sort the list in ascending order by name
        notifyDataSetChanged()
    }
}
