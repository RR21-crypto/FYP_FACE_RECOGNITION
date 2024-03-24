package com.example.facerecognition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facerecognition.adapter.RegisteredFaceAdapter
import com.example.facerecognition.databinding.ActivityFaceListBinding

class FaceListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.faceListRecyclerView
        showRecyclerList()

        binding.backButton.setOnClickListener {
            this.finish()
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