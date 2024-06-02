package com.example.facerecognition.Activity

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
                    binding.detailAttendance.text = attendants.mapIndexed { index, it ->
                        val formattedDate = roomHelper.convertDate(it.attendanceEntity.attendanceDate)
                        val formattedHour = roomHelper.convertHour(it.attendanceEntity.attendanceDate)
                        "No ${index + 1}:  |      Date: $formattedDate  |    Hour : $formattedHour"
                    }.joinToString("\n")
                }
            }
        }
    }

}