package com.example.facerecognition.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.Helper.RoomHelper
import com.example.facerecognition.R

import com.example.facerecognition.adapter.RegisteredFaceAdapter
import com.example.facerecognition.databinding.FragmentRegisteredBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisteredFragment : Fragment() {
    private lateinit var binding: FragmentRegisteredBinding
    private val faceRecognitionHelper = FaceRecognitionHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisteredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            showRecyclerList()
        }
        binding.clearAllButton.setOnClickListener {
            faceRecognitionHelper.clearFace(requireContext())
            Toast.makeText(requireContext(), "succes deleted", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private suspend fun showRecyclerList() {
        binding.faceListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val roomHelper = RoomHelper()
        roomHelper.init(requireContext())
        val registeredFace = roomHelper.getALLStudentList()
        val mappedRegisteredStudnet = registeredFace.map {
            RegisteredFace(
                it.name,
                it.embedding.split(";").map { it.toFloat() }.toFloatArray(),
                it.date,
                it.matric
            )
        }
        withContext(Dispatchers.Main) {
            val taskAdapter =
                RegisteredFaceAdapter(mappedRegisteredStudnet, roomHelper, requireContext())
            binding.faceListRecyclerView.adapter = taskAdapter
        }
    }
}