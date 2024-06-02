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
        binding.faceListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
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
        }
        withContext(Dispatchers.Main) {
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
        }
        taskAdapter.updateList(filteredList)
        return true
    }
}
