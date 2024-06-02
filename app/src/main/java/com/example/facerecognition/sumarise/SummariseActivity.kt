package com.example.facerecognition.sumarise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facerecognition.Helper.RoomHelper
import com.example.facerecognition.R
import com.example.facerecognition.adapter.SummariseAdapter
import com.example.facerecognition.adapter.SummariseItem
import com.example.facerecognition.databinding.ActivitySummariseBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class SummariseActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummariseBinding
    private lateinit var roomHelper: RoomHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummariseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // enableEdgeToEdge() // Hapus atau tambahkan definisi fungsi jika diperlukan
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        roomHelper = RoomHelper()
        roomHelper.init(this)

        lifecycleScope.launch {
            val sameDayAttendees = getSameDayAttendees()
            setupRecyclerView(sameDayAttendees)
        }
    }

    private suspend fun getSameDayAttendees(): List<SummariseItem> {
        return withContext(Dispatchers.IO) {
            val attendanceList = roomHelper.getAttendantList()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

            attendanceList.groupBy { attendance ->
                dateFormat.format(attendance.attendanceEntity.attendanceDate)
            }.flatMap { (date, sameDayList) ->
                listOf(SummariseItem.DateItem(date)) + sameDayList
                    .distinctBy { it.studentEntity.matric }
                    .map {
                        val time = timeFormat.format(it.attendanceEntity.attendanceDate)
                        SummariseItem.AttendeeItem(it.studentEntity.matric, it.studentEntity.name, time)
                    }
            }
        }
    }

    private fun setupRecyclerView(items: List<SummariseItem>) {
        val adapter = SummariseAdapter(items)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}
