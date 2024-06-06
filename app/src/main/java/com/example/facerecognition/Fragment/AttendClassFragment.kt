package com.example.facerecognition.Fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facerecognition.Helper.RoomHelper
import com.example.facerecognition.R
import com.example.facerecognition.adapter.AttendedFaceRegisterAdapter
import com.example.facerecognition.databinding.FragmentAttendClassBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AttendClassFragment : Fragment() {
    private lateinit var binding: FragmentAttendClassBinding
    private val roomHelper = RoomHelper()
    private lateinit var adapter: AttendedFaceRegisterAdapter

    private val updateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.example.facerecognition.UPDATE_NAME") {
                CoroutineScope(Dispatchers.IO).launch {
                    showRecyclerList()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAttendClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            roomHelper.init(requireContext())
            showRecyclerList()
        }

        binding.attendClearAllButton.setOnLongClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                roomHelper.clearAllAttendance(requireContext())
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "success deleted", Toast.LENGTH_SHORT).show()
                    showRecyclerList()
                }
            }
            true
        }

        // Register receiver
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(updateReceiver, IntentFilter("com.example.facerecognition.UPDATE_NAME"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Unregister receiver
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(updateReceiver)
    }

    fun refreshList() {
        CoroutineScope(Dispatchers.IO).launch {
            val registeredFace = roomHelper.getAttendantList().reversed() // Reverse the list
            withContext(Dispatchers.Main) {
                adapter.setNewList(registeredFace)
                binding.attendFaceListRecyclerView.scrollToPosition(0)
            }
        }
    }

    private suspend fun showRecyclerList() {
        withContext(Dispatchers.Main) {
            binding.attendFaceListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        roomHelper.init(requireContext())
        val registeredFace = roomHelper.getAttendantList().reversed() // Reverse the list to show latest first
        withContext(Dispatchers.Main) {
            adapter = AttendedFaceRegisterAdapter(roomHelper, registeredFace, requireContext())
            binding.attendFaceListRecyclerView.adapter = adapter
            binding.attendFaceListRecyclerView.scrollToPosition(0)
        }
    }
}
