package com.example.facerecognition.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.facerecognition.Entity.AttendanceWithStudentEntity
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.Helper.RoomHelper
import com.example.facerecognition.R
import com.example.facerecognition.databinding.LayoutAttendantBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class AttendedFaceRegisterAdapter(
    private val roomHelper: RoomHelper,
    private val listStudent: List<AttendanceWithStudentEntity>,
    private val context: Context
) : RecyclerView.Adapter<AttendedFaceRegisterAdapter.ListViewHolder>() {

    private val faceRecognitionHelper = FaceRecognitionHelper()
    private val listStudents: MutableList<AttendanceWithStudentEntity> = listStudent.toMutableList()

    class ListViewHolder(private val binding: LayoutAttendantBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setView(attendance: AttendanceWithStudentEntity, dateFormat: SimpleDateFormat, context: Context) {
            binding.attendUsernameTextView.text = attendance.studentEntity.name
            val formatDate = dateFormat.format(attendance.attendanceEntity.getAttendanceDateAsDate())
            binding.attendRegisterDateTextView.text = formatDate
            binding.attendMatricNumberTextView.text = attendance.attendanceEntity.studentMatrics

            // Ubah warna CardView dekorasi berdasarkan jenis kehadiran
            if (attendance.attendanceEntity.type == "OUT") {
                binding.cardviewDecoration.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue))
            } else {
                binding.cardviewDecoration.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pastel_coral))
            }
        }

        val tvdelete: ImageButton = binding.attendDeleteButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutAttendantBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return listStudents.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val face = listStudents[position]
        holder.setView(face, SimpleDateFormat("dd-MM-yyyy | HH:mm", Locale.getDefault()), context)

        holder.tvdelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                roomHelper.spesificAttendanceDelete(context, face.attendanceEntity.studentMatrics)
                withContext(Dispatchers.Main) {
                    listStudents.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, listStudents.size)
                }
            }
        }
    }

    fun setNewList(list: List<AttendanceWithStudentEntity>) {
        listStudents.clear()
        listStudents.addAll(list.reversed()) // Reverse the list to show latest first
        notifyDataSetChanged()
    }
}
