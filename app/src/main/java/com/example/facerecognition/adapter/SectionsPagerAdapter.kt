package com.example.facerecognition.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.facerecognition.Fragment.AttendClassFragment
import com.example.facerecognition.Fragment.RegisteredFragment

class SectionsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    val registeredFragment = RegisteredFragment(onDeleteListener = { refreshAttendance() })
    val attendanceFragment = AttendClassFragment()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> registeredFragment
            1 -> attendanceFragment
            else -> throw Exception("")
        }
    }

    fun refreshAttendance() {
        attendanceFragment.refreshList()
    }
}


