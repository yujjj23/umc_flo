package com.example.myapplication


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

//class SearchPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
//
//    private val fragments = listOf(TrackFragment(), DetailFragment(), VideoFragment())
//
//    override fun getItemCount(): Int = fragments.size
//
//    override fun createFragment(position: Int): Fragment {
//        return fragments[position]
//    }
//
//}


class SearchPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2 // 탭 개수

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedSongsFragment()  // 저장한 곡 프래그먼트
            1 -> MusicFilesFragment()  // 음악 파일 프래그먼트
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}

