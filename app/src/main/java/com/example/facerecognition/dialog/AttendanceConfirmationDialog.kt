package com.example.facerecognition.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.facerecognition.Entity.AttendanceEntity
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.Helper.RoomHelper
import com.example.facerecognition.R
import com.example.facerecognition.databinding.DialogAttendanceConfirmationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AttendanceConfirmationDialog() : DialogFragment() {

    private lateinit var binding: DialogAttendanceConfirmationBinding
    var registeredFace: RegisteredFace? = null
    var score: Double? = null
    private var turnOffAttendanceModeListener: (() -> Unit)? = null
    private val roomHelper = RoomHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAttendanceConfirmationBinding.inflate(inflater, container, false)
        val dialogView = binding.root

        // Set the background color
        dialogView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))

        return dialogView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomHelper.init(requireContext())

        binding.buttonTurnOffAttendanceMode.setOnClickListener {
            turnOffAttendanceModeListener?.invoke()
        }
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    fun setData() {
        binding.textViewStudentName.text = registeredFace?.name
        binding.textViewStudentMatric.text = registeredFace?.matric
        binding.textViewProbability.text = "Probability: ${score}"
        binding.buttonSaveAttendance.setOnClickListener {
//            dismiss()
            saveAttendance()
        }
    }

    fun setOnTurnOffAttendanceModeListener(listener: () -> Unit) {
        this.turnOffAttendanceModeListener = listener
    }

    private fun saveAttendance() {
        registeredFace?.let {
            CoroutineScope(Dispatchers.IO).launch {
                roomHelper.inserrAttendance(AttendanceEntity(id = 0, studentMatrics =it.matric, attendanceDate = System.currentTimeMillis()))

                withContext(Dispatchers.Main) {
                    this@AttendanceConfirmationDialog.dismiss()
                }
            }
        }
    }



}