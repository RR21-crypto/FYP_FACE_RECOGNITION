package com.example.facerecognition.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.facerecognition.Fragment.AttendClassFragment
import com.example.facerecognition.Fragment.RegisteredFragment

class SectionsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = RegisteredFragment()
            1 -> fragment = AttendClassFragment()
        }
        return fragment as Fragment
    }
    }


