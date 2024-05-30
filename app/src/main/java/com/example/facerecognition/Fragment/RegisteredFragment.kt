package com.example.facerecognition.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView // <-- Highlighted
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facerecognition.Activity.DetailActivity
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.Helper.RoomHelper
import com.example.facerecognition.R

import com.example.facerecognition.adapter.RegisteredFaceAdapter
import com.example.facerecognition.databinding.FragmentRegisteredBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisteredFragment(
    val onDeleteListener: () -> Unit
) : Fragment(), SearchView.OnQueryTextListener { // <-- Highlighted
    private lateinit var binding: FragmentRegisteredBinding
    private val faceRecognitionHelper = FaceRecognitionHelper()
    private lateinit var taskAdapter: RegisteredFaceAdapter // <-- Highlighted
    private var registeredFaces = listOf<RegisteredFace>() // <-- Highlighted

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

        binding.searchBar.setOnQueryTextListener(this) // <-- Highlighted
    }

    private suspend fun showRecyclerList() {
        binding.faceListRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        val roomHelper = RoomHelper()
        roomHelper.init(requireContext())
        val registeredFace = roomHelper.getALLStudentList()
        val attendantFace = roomHelper.getAttendantList()
        registeredFaces = registeredFace.map { // <-- Highlighted
            RegisteredFace(
                it.name,
                it.embedding.split(";").map { it.toFloat() }.toFloatArray(),
                it.date,
                it.matric
            )
        }
        withContext(Dispatchers.Main) {
            taskAdapter = RegisteredFaceAdapter(registeredFaces, roomHelper, requireContext(), attendantFace) // <-- Highlighted
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

    override fun onQueryTextChange(newText: String?): Boolean { // <-- Highlighted
        val filteredList = registeredFaces.filter { // <-- Highlighted
            it.name.contains(newText ?: "", ignoreCase = true) // <-- Highlighted
        }
        taskAdapter.updateList(filteredList) // <-- Highlighted
        return true // <-- Highlighted
    }
}
