package com.example.facerecognition.Fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
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

class RegisteredFragment(
    val onDeleteListener: () -> Unit
) : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentRegisteredBinding
    private val faceRecognitionHelper = FaceRecognitionHelper()
    private lateinit var taskAdapter: RegisteredFaceAdapter
    private var registeredFaces = listOf<RegisteredFace>()

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
}
