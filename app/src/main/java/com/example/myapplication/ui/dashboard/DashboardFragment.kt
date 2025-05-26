package com.example.myapplication.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.DashboardPagerAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDashboardBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabTextViews: List<TextView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        viewPager = view.findViewById(R.id.look_view_pager)

        tabTextViews = listOf(
            view.findViewById(R.id.look_chip_title_01_tv),
            view.findViewById(R.id.look_chip_title_02_tv),
            view.findViewById(R.id.look_chip_title_03_tv),
            view.findViewById(R.id.look_chip_title_04_tv),
            view.findViewById(R.id.look_chip_title_05_tv),
            view.findViewById(R.id.look_chip_title_06_tv)
        )

        // ViewPager 연결
        val adapter = DashboardPagerAdapter(this)
        viewPager.adapter = adapter

        // 탭 클릭 시 ViewPager 이동
        tabTextViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                viewPager.currentItem = index
            }
        }

        // ViewPager 스와이프 시 탭 UI 변경
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTabStyles(position)
            }
        })

        // 초기 탭 스타일 설정
        updateTabStyles(0)

        return view
    }

    private fun updateTabStyles(selectedPosition: Int) {
        tabTextViews.forEachIndexed { index, textView ->
            if (index == selectedPosition) {
                textView.setTextColor(Color.WHITE)
                textView.setBackgroundResource(R.drawable.fragment_look_chip_on_background)
            } else {
                textView.setTextColor(Color.parseColor("#7D7D7D"))
                textView.setBackgroundResource(R.drawable.fragment_look_chip_off_background)
            }
        }
    }
}


