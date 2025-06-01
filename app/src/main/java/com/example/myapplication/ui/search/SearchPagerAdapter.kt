package com.example.myapplication.ui.search


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.ui.musicplayer.MusicFilesFragment


class SearchPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3 // 탭 개수

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedSongsFragment()  // 저장한 곡 프래그먼트
            1 -> MusicFilesFragment()  // 음악 파일 프래그먼트
            2 -> SavedAlbumsFragment()  // 저장 앨범 프래그먼트
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}

