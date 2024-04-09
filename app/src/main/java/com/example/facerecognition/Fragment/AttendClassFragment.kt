package com.example.facerecognition.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.R
import com.example.facerecognition.StorageHelper
import com.example.facerecognition.adapter.RegisteredFaceAdapter
import com.example.facerecognition.databinding.FragmentAttendClassBinding
import com.example.facerecognition.databinding.FragmentRegisteredBinding


class AttendClassFragment : Fragment() {
    private lateinit var binding: FragmentAttendClassBinding
    private val faceRecognitionHelper = FaceRecognitionHelper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAttendClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()

        binding.attendClearAllButton.setOnClickListener {
            faceRecognitionHelper.clearFace(requireContext())
            Toast.makeText(requireContext(), "succes deleted", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun showRecyclerList() {
        binding.attendFaceListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val storageHelper = StorageHelper()
        val registeredFace = storageHelper.getRegisterFace(requireContext())
        val taskAdapter = RegisteredFaceAdapter(registeredFace, storageHelper, requireContext())
        binding.attendFaceListRecyclerView.adapter = taskAdapter
    }
}