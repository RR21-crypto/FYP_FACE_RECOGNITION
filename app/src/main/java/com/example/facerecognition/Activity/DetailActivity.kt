package com.example.facerecognition.activity

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.Helper.RoomHelper
import com.example.facerecognition.R
import com.example.facerecognition.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    lateinit var data: RegisteredFace
    private lateinit var roomHelper: RoomHelper
    private lateinit var binding: ActivityDetailBinding
    private var isEditVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RoomHelper
        roomHelper = RoomHelper()
        roomHelper.init(this)
        window.statusBarColor = resources.getColor(R.color.light_blue, theme)

        // Retrieve the RegisteredFace object from the intent extras
        val detailActivity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("DATA", RegisteredFace::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<RegisteredFace>("DATA")
        }

        if (detailActivity != null) {
            data = detailActivity
            binding.detailName.text = data.name
            binding.detailMatrics.text = data.matric
            data.imageUri?.let { uri ->
                Glide.with(this).load(Uri.parse(uri)).into(binding.detailImage)
            }

            // Fetch and display attendance list
            CoroutineScope(Dispatchers.IO).launch {
                val attendants = roomHelper.getAttendantListByMatrics(data.matric)
                withContext(Dispatchers.Main) {
                    val inAttendances = attendants.filter { it.type == "IN" }
                    val outAttendances = attendants.filter { it.type == "OUT" }

                    binding.detailAttendance.text = inAttendances.mapIndexed { index, it ->
                        val formattedDate = roomHelper.convertDate(it.attendanceDate)
                        val formattedHour = roomHelper.convertHour(it.attendanceDate)
                        "No ${index + 1}:  |      Date: $formattedDate  |    Hour : $formattedHour"
                    }.joinToString("\n")

                    binding.detailOutattendance.text = outAttendances.mapIndexed { index, it ->
                        val formattedDate = roomHelper.convertDate(it.attendanceDate)
                        val formattedHour = roomHelper.convertHour(it.attendanceDate)
                        "No ${index + 1}:  |      Date: $formattedDate  |    Hour : $formattedHour"
                    }.joinToString("\n")

                    // Animate the attendance fields
                    animateAttendanceFields()
                }
            }
        }

        // Hide the edit fields initially
        binding.editNameText.visibility = View.GONE
        binding.editButton.visibility = View.GONE

        // Set click listener for the trigger button
        binding.triggerEditButton.setOnClickListener {
            toggleEditFields()
        }

        // Set click listener for the save button
        binding.editButton.setOnClickListener {
            val newName = binding.editNameText.text.toString()
            if (newName.isNotEmpty()) {
                data.name = newName
                binding.detailName.text = newName
                // Update the name in the database
                CoroutineScope(Dispatchers.IO).launch {
                    roomHelper.updateStudentName(data.matric, newName)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DetailActivity, "Name updated successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleEditFields() {
        if (isEditVisible) {
            binding.editNameText.animate()
                .translationX(binding.editNameText.width.toFloat())
                .alpha(0f)
                .setDuration(300)
                .withEndAction { binding.editNameText.visibility = View.GONE }
            binding.editButton.animate()
                .translationX(binding.editButton.width.toFloat())
                .alpha(0f)
                .setDuration(300)
                .withEndAction { binding.editButton.visibility = View.GONE }
        } else {
            binding.editNameText.visibility = View.VISIBLE
            binding.editNameText.alpha = 0f
            binding.editNameText.translationX = binding.editNameText.width.toFloat()
            binding.editNameText.animate()
                .translationX(0f)
                .alpha(1f)
                .setDuration(300)
            binding.editButton.visibility = View.VISIBLE
            binding.editButton.alpha = 0f
            binding.editButton.translationX = binding.editButton.width.toFloat()
            binding.editButton.animate()
                .translationX(0f)
                .alpha(1f)
                .setDuration(300)
        }
        isEditVisible = !isEditVisible
    }

    private fun animateAttendanceFields() {
        binding.detailAttendance.translationY = 100f
        binding.detailAttendance.alpha = 0f
        binding.detailAttendance.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(500)
            .setStartDelay(300)
            .start()

        binding.detailOutattendance.translationY = 100f
        binding.detailOutattendance.alpha = 0f
        binding.detailOutattendance.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(500)
            .setStartDelay(600)
            .start()
    }
}
