package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = listOf(TrackFragment(), DetailFragment(), VideoFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}
