package com.example.facerecognition.sumarise

import android.app.DatePickerDialog
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
import java.util.Calendar
import java.util.Locale

class SummariseActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummariseBinding
    private lateinit var roomHelper: RoomHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummariseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        roomHelper = RoomHelper()
        roomHelper.init(this)

        binding.btnPickDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Fetch the latest attendance on initial load
        fetchLatestAttendance()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val dateString = dateFormat.format(selectedDate.time)
                fetchAttendanceForDate(dateString)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun fetchLatestAttendance() {
        lifecycleScope.launch {
            val latestDate = getLatestAttendanceDate()
            if (latestDate != null) {
                fetchAttendanceForDate(latestDate)
            }
        }
    }

    private fun fetchAttendanceForDate(date: String) {
        lifecycleScope.launch {
            val attendees = getAttendeesForDate(date)
            val totalStudents = getTotalStudents()
            val itemsWithAttendanceInfo = addAttendanceInfoToItems(attendees, totalStudents)
            setupRecyclerView(itemsWithAttendanceInfo)
        }
    }

    private suspend fun getLatestAttendanceDate(): String? {
        return withContext(Dispatchers.IO) {
            val attendanceList = roomHelper.getAttendantList()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            attendanceList.map {
                dateFormat.format(it.attendanceEntity.attendanceDate)
            }.distinct().maxOrNull()
        }
    }

    private suspend fun getAttendeesForDate(date: String): List<SummariseItem> {
        return withContext(Dispatchers.IO) {
            val attendanceList = roomHelper.getAttendantList()
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

            val filteredList = attendanceList.filter {
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                dateFormat.format(it.attendanceEntity.attendanceDate) == date
            }.distinctBy { it.studentEntity.matric }

            listOf(SummariseItem.DateItem(date, "")) + filteredList.map {
                val time = timeFormat.format(it.attendanceEntity.attendanceDate)
                SummariseItem.AttendeeItem(it.studentEntity.matric, it.studentEntity.name, time, date)
            }
        }
    }

    private fun addAttendanceInfoToItems(items: List<SummariseItem>, totalStudents: Int): List<SummariseItem> {
        val dateItems = items.filterIsInstance<SummariseItem.DateItem>()
        val attendeeItems = items.filterIsInstance<SummariseItem.AttendeeItem>()

        val updatedDateItems = dateItems.map { dateItem ->
            val attendeesCount = attendeeItems.count { it.date == dateItem.date }
            dateItem.copy(attendanceInfo = "Attendance: $attendeesCount/$totalStudents")
        }

        return updatedDateItems + attendeeItems
    }

    private fun setupRecyclerView(items: List<SummariseItem>) {
        val adapter = SummariseAdapter(items)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private suspend fun getTotalStudents(): Int {
        return withContext(Dispatchers.IO) {
            roomHelper.getALLStudentList().size
        }
    }
}
