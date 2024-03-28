package com.example.facerecognition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.facerecognition.adapter.RegisteredFaceAdapter
import com.example.facerecognition.adapter.SectionsPagerAdapter
import com.example.facerecognition.databinding.ActivityFaceListBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class FaceListActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private lateinit var binding: ActivityFaceListBinding
    private  val faceRecognitionHelper = FaceRecognitionHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceListBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.faceListRecyclerView
        showRecyclerList()

        binding.backButton.setOnClickListener {
            this.finish()
        }




        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }


    }

    private fun showRecyclerList() {
//        binding.faceListRecyclerView.layoutManager = LinearLayoutManager(this)
//        val storageHelper = StorageHelper()
//        val registeredFace = storageHelper.getRegisterFace(this)
//        val taskAdapter = RegisteredFaceAdapter(registeredFace,storageHelper,this)
//        binding.faceListRecyclerView.adapter = taskAdapter
    }
