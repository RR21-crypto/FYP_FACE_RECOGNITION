package com.example.facerecognition.Activity

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.R

class DetailActivity : AppCompatActivity() {

    lateinit var data: RegisteredFace

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        // Retrieve the RegisteredFace object from the intent extras
        val detailActivity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("DATA", RegisteredFace::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<RegisteredFace>("DATA")
        }

        if (detailActivity != null) {
            data = detailActivity
            // Now you can use the 'data' object to access the information sent from RegisteredFragment
            // For example, set the data to your views
            findViewById<TextView>(R.id.detail_name).text = data.name
            findViewById<TextView>(R.id.detail_matrics).text = data.matric
            // Set other fields as needed
        }
    }
}
