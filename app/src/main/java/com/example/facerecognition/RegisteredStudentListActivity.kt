package com.example.facerecognition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facerecognition.adapter.RegisteredFaceAdapter
import com.example.facerecognition.databinding.ActivityRegisteredStudentListBinding

class RegisteredStudentListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisteredStudentListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding object
        binding = ActivityRegisteredStudentListBinding.inflate(layoutInflater)

        // Set the content view to the root view of the activity
        setContentView(binding.root)

        // Set the click listener for the back button

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.userListRv.setHasFixedSize(true)


        showRecyclerList()

        val storageHelper = StorageHelper()

    }



    private fun showRecyclerList() {
        binding.userListRv.layoutManager = LinearLayoutManager(this)
        val storageHelper = StorageHelper()
        val registeredFace = storageHelper.getRegisterFace(this)
        val taskAdapter = RegisteredFaceAdapter(registeredFace,storageHelper,this)
        binding.userListRv.adapter = taskAdapter
    }
}