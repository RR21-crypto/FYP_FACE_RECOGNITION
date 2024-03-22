package com.example.facerecognition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.facerecognition.databinding.ActivityMainBinding
import com.example.facerecognition.databinding.ActivityRegisteredStudentListBinding

class RegisteredStudentList : AppCompatActivity() {

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

    }
}