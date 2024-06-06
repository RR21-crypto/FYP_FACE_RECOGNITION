package com.example.facerecognition.Fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.Helper.RoomHelper
import com.example.facerecognition.R
import com.example.facerecognition.activity.DetailActivity
import com.example.facerecognition.adapter.RegisteredFaceAdapter
import com.example.facerecognition.databinding.FragmentRegisteredBinding
import com.example.facerecognition.sumarise.SummariseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Field
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import android.os.Environment

class RegisteredFragment(
    val onDeleteListener: () -> Unit
) : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentRegisteredBinding
    private val faceRecognitionHelper = FaceRecognitionHelper()
    private lateinit var taskAdapter: RegisteredFaceAdapter
    private var registeredFaces = listOf<RegisteredFace>()
    private var scrollDy = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisteredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            showRecyclerList()
        }

        binding.searchBar.setOnQueryTextListener(this)
        setSearchViewTextColor(binding.searchBar, Color.BLACK)

        // Tambahkan listener untuk tombol summarise
        binding.summarise.setOnClickListener {
            val intentToSummarise = Intent(requireContext(), SummariseActivity::class.java)
            startActivity(intentToSummarise)
        }

        // Tambahkan listener untuk tombol refresh
        binding.refresh.setOnClickListener {
            refreshBothFragments()
        }

        // Tambahkan listener untuk tombol export
        binding.export.setOnClickListener {
            checkSdkAndExportToExcel(requireContext())
        }

        // Set scroll listener
        binding.faceListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    // Scrolling down
                    hideButtons()
                } else if (dy < 0) {
                    // Scrolling up
                    showButtons()
                }
                scrollDy = dy
            }
        })
    }

    private fun hideButtons() {
        binding.summarise.hide()
        binding.refresh.hide()
        binding.export.hide()
    }

    private fun showButtons() {
        binding.summarise.show()
        binding.refresh.show()
        binding.export.show()
    }

    private fun setSearchViewTextColor(searchView: SearchView, color: Int) {
        try {
            val searchAutoCompleteField: Field = SearchView::class.java.getDeclaredField("mSearchSrcTextView")
            searchAutoCompleteField.isAccessible = true
            val searchAutoComplete = searchAutoCompleteField.get(searchView) as SearchView.SearchAutoComplete
            searchAutoComplete.setTextColor(color)
            searchAutoComplete.setHintTextColor(color)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun showRecyclerList() {
        val roomHelper = RoomHelper()
        roomHelper.init(requireContext())
        val registeredFace = roomHelper.getALLStudentList()
        val attendantFace = roomHelper.getAttendantList()
        registeredFaces = registeredFace.map {
            RegisteredFace(
                it.name,
                it.embedding.split(";").map { it.toFloat() }.toFloatArray(),
                it.date,
                it.matric,
                it.imageUri
            )
        }.sortedBy { it.name } // Sort the list in ascending order by name
        withContext(Dispatchers.Main) {
            binding.faceListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            taskAdapter = RegisteredFaceAdapter(registeredFaces, roomHelper, requireContext(), attendantFace)
            taskAdapter.setOnItemClickCallback(object : RegisteredFaceAdapter.OnItemClickCallback {
                override fun onItemClicked(data: RegisteredFace) {
                    showSelectedStudent(data)
                    val intentToDetail = Intent(requireContext(), DetailActivity::class.java)
                    intentToDetail.putExtra("DATA", data)
                    startActivity(intentToDetail)
                }
            })
            taskAdapter.setOnDeleteListener {
                onDeleteListener.invoke()
            }
            binding.faceListRecyclerView.adapter = taskAdapter
        }
    }

    private fun showSelectedStudent(registeredFace: RegisteredFace) {
        val message = "You selected ${registeredFace.name}"
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val filteredList = registeredFaces.filter {
            it.name.contains(newText ?: "", ignoreCase = true)
        }.sortedBy { it.name } // Sort filtered list by name
        taskAdapter.updateList(filteredList)
        return true
    }

    private fun refreshBothFragments() {
        // Refresh the current fragment
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                showRecyclerList()
            }
        }

        // Broadcast refresh action to AttendClassFragment
        val intent = Intent("com.example.facerecognition.UPDATE_NAME")
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
    }

    private fun checkSdkAndExportToExcel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Kode untuk mengekspor ke Excel
            CoroutineScope(Dispatchers.IO).launch {
                exportToExcel()
            }
        } else {
            // Menampilkan toast jika versi SDK tidak didukung
            Toast.makeText(context, "Fitur ini tidak didukung di versi Android ini", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun exportToExcel() {
        val roomHelper = RoomHelper()
        roomHelper.init(requireContext())
        val attendanceList = roomHelper.getAttendantList()

        // Create a workbook
        val workbook: Workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Attendance")

        // Create a header row
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Name")
        headerRow.createCell(1).setCellValue("Matric Number")
        headerRow.createCell(2).setCellValue("Date")
        headerRow.createCell(3).setCellValue("Time")
        headerRow.createCell(4).setCellValue("Type")

        // Create a date format
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        // Populate the data rows
        attendanceList.forEachIndexed { index, attendanceWithStudent ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(attendanceWithStudent.studentEntity.name)
            row.createCell(1).setCellValue(attendanceWithStudent.attendanceEntity.studentMatrics)

            val date = attendanceWithStudent.attendanceEntity.getAttendanceDateAsDate()
            row.createCell(2).setCellValue(dateFormat.format(date))

            val time = attendanceWithStudent.attendanceEntity.getAttendanceDateAsDate()
            row.createCell(3).setCellValue(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(time))

            row.createCell(4).setCellValue(attendanceWithStudent.attendanceEntity.type)
        }

        // Write the output to a file
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Attendance.xlsx")
        FileOutputStream(file).use { outputStream ->
            workbook.write(outputStream)
            outputStream.close()
        }

        withContext(Dispatchers.Main) {
            Toast.makeText(requireContext(), "Attendance exported to Excel", Toast.LENGTH_SHORT).show()
        }
    }
}
