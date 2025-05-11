package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumPagerAdapter(
    fragment: Fragment,
    private val album: Album // 앨범 데이터를 생성자로 받음
) : FragmentStateAdapter(fragment) {

    private val fragments = listOf(TrackFragment(), DetailFragment(), VideoFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        val fragment = fragments[position]

        // 각 프래그먼트에 앨범 데이터를 전달
        when (fragment) {
            is TrackFragment -> fragment.setAlbumData(album)
            is DetailFragment -> fragment.setAlbumData(album)
            is VideoFragment -> fragment.setAlbumData(album)
        }

        return fragment
    }
}

