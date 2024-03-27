package com.example.facerecognition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facerecognition.adapter.RegisteredFaceAdapter
import com.example.facerecognition.databinding.ActivityFaceListBinding


class FaceListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceListBinding
    private  val faceRecognitionHelper = FaceRecognitionHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.faceListRecyclerView
        showRecyclerList()

        binding.backButton.setOnClickListener {
            this.finish()
        }


        binding.clearAllButton.setOnClickListener {
            faceRecognitionHelper.clearFace(this)
            Toast.makeText(this, "succes deleted", Toast.LENGTH_SHORT).show()
            true
        }

    }

    private fun showRecyclerList() {
        binding.faceListRecyclerView.layoutManager = LinearLayoutManager(this)
        val storageHelper = StorageHelper()
        val registeredFace = storageHelper.getRegisterFace(this)
        val taskAdapter = RegisteredFaceAdapter(registeredFace)
        binding.faceListRecyclerView.adapter = taskAdapter
    }
}