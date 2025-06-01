package com.example.myapplication.ui.dashboard

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DashboardPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChartFragment()
            1 -> Dashboard_VideoFragment()
            2 -> GenreFragment()
            3 -> SituationFragment()
            4 -> MoodFragment()
            5 -> AudioFragment()
            else -> Fragment()
        }
    }
}