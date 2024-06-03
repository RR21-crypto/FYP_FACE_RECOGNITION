package com.example.facerecognition.Activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
                    val inAttendances = attendants.filterIndexed { index, _ -> (index + 1) % 2 != 0 }
                    val outAttendances = attendants.filterIndexed { index, _ -> (index + 1) % 2 == 0 }

                    binding.detailAttendance.text = inAttendances.mapIndexed { index, it ->
                        val formattedDate = roomHelper.convertDate(it.attendanceEntity.attendanceDate)
                        val formattedHour = roomHelper.convertHour(it.attendanceEntity.attendanceDate)
                        "No ${index + 1}:  |      Date: $formattedDate  |    Hour : $formattedHour"
                    }.joinToString("\n")

                    binding.detailOutattendance.text = outAttendances.mapIndexed { index, it ->
                        val formattedDate = roomHelper.convertDate(it.attendanceEntity.attendanceDate)
                        val formattedHour = roomHelper.convertHour(it.attendanceEntity.attendanceDate)
                        "No ${index + 1}:  |      Date: $formattedDate  |    Hour : $formattedHour"
                    }.joinToString("\n")
                }
            }
        }

        // Set up the edit button listener
        binding.editButton.setOnClickListener {
            val newName = binding.editNameText.text.toString()
            if (newName.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    roomHelper.updateStudentName(data.matric, newName)
                    withContext(Dispatchers.Main) {
                        binding.detailName.text = newName
                        Toast.makeText(this@DetailActivity, "Name updated", Toast.LENGTH_SHORT).show()

                        // Update local data
                        data.name = newName

                        // Return the result to RegisteredFragment
                        val resultIntent = Intent().apply {
                            putExtra("UPDATED_NAME", newName)
                            putExtra("MATRIC", data)
                        }
                        setResult(RESULT_OK, resultIntent)
                    }
                }
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
