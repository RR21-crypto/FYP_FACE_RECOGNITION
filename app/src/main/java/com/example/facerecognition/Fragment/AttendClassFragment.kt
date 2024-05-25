package com.example.facerecognition.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facerecognition.Database.StudentDatabase
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.Helper.RoomHelper
import com.example.facerecognition.R
import com.example.facerecognition.adapter.AttendedFaceRegisterAdapter

import com.example.facerecognition.adapter.RegisteredFaceAdapter
import com.example.facerecognition.databinding.FragmentAttendClassBinding
import com.example.facerecognition.databinding.FragmentRegisteredBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AttendClassFragment : Fragment() {
    private lateinit var binding: FragmentAttendClassBinding
    private val faceRecognitionHelper = FaceRecognitionHelper()
    private val roomHelper =RoomHelper()




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAttendClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val roomHelper = RoomHelper()
            roomHelper.init(requireContext())

            showRecyclerList()
        }
        

//            binding.attendClearAllButton.setOnClickListener {
//                roomHelper.clearAllAttendance(requireContext())
//                withContext(Dispatchers.Main) {
//                    // Update the RecyclerView
//                    showRecyclerList()
//                }
//            }

            binding.attendClearAllButton.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    roomHelper.clearAllAttendance(requireContext())
                    withContext(Dispatchers.Main) {
                    // Update the RecyclerView
                    showRecyclerList()
                 }
                }

            }


    }

    private suspend fun showRecyclerList() {
        binding.attendFaceListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val roomHelper = RoomHelper()
        roomHelper.init(requireContext())
        val registeredFace = roomHelper.getAttendantList()
        withContext(Dispatchers.Main) {
            val taskAdapter = AttendedFaceRegisterAdapter(roomHelper, registeredFace, requireContext())
            binding.attendFaceListRecyclerView.adapter = taskAdapter
        }

    }
}