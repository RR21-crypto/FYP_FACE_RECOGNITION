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
import com.example.facerecognition.databinding.FragmentRegisteredBinding




class RegisteredFragment : Fragment() {
    private lateinit var binding: FragmentRegisteredBinding
    private val faceRecognitionHelper = FaceRecognitionHelper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisteredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()

        binding.clearAllButton.setOnClickListener {
            faceRecognitionHelper.clearFace(requireContext())
            Toast.makeText(requireContext(), "succes deleted", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun showRecyclerList() {
        binding.faceListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val storageHelper = StorageHelper()
        val registeredFace = storageHelper.getRegisterFace(requireContext())
        val taskAdapter = RegisteredFaceAdapter(registeredFace, storageHelper, requireContext())
        binding.faceListRecyclerView.adapter = taskAdapter
    }
}