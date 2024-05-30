package com.example.facerecognition

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.facerecognition.adapter.SectionsPagerAdapter
import com.example.facerecognition.databinding.ActivityFaceListBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FaceListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceListBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            this.finish()
        }
        window.statusBarColor = resources.getColor(R.color.blue, theme)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        viewPager.offscreenPageLimit = 1
        viewPager.isUserInputEnabled = true  // Enable swipe
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.animate()?.scaleX(1.1f)?.scaleY(1.1f)?.setDuration(300)?.start()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(300)?.start()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}
