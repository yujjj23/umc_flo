package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AlbumFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var btnBack: Button // 뒤로가기 버튼
    private var albumTitle: String? = null
    private var imageResId: Int = R.drawable.default_image  // 기본 이미지를 설정

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_album, container, false)

        // UI 요소들 초기화
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)
        btnBack = view.findViewById(R.id.btn_back)

        // 뒤로가기 버튼 클릭 처리
        btnBack.setOnClickListener {
            requireActivity().onBackPressed() // 뒤로가기
        }

        // ViewPager 어댑터 설정
        val adapter = AlbumPagerAdapter(this)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager 연동
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "수록곡"
                1 -> tab.text = "상세정보"
                2 -> tab.text = "동영상"
            }
        }.attach()

        // TrackFragment에서 ivExtraImage를 참조하기 위해
        // 이 부분은 TrackFragment에서 접근하도록 해야합니다.
        // 아래 코드에서 ivExtraImage는 TrackFragment에서 정의되어야 합니다.
        // 따로 초기화가 필요하다면 이를 TrackFragment로 이동해야 합니다.



        albumTitle = arguments?.getString("ALBUM_TITLE") ?: "앨범 정보 없음"
        imageResId = arguments?.getInt("IMAGE_RES_ID") ?: R.drawable.default_image

        // ImageView에 이미지 설정
        val imageView = view.findViewById<ImageView>(R.id.iv_center_image)
        imageView.setImageResource(imageResId) // 전달된 이미지 리소스 ID 설정

        // 앨범 제목 설정
        val titleTextView = view.findViewById<TextView>(R.id.tv_album_title)
        titleTextView.text = albumTitle // 전달된 앨범 제목 설정

        // 뒤로 가기 버튼 처리
        val backButton = view.findViewById<Button>(R.id.btn_back)
        backButton.setOnClickListener {
            // 홈 화면으로 이동
            findNavController().navigate(R.id.action_albumFragment_to_homeFragment)
        }

        // 물리적인 뒤로가기 버튼을 처리
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_albumFragment_to_homeFragment)
        }

        return view
    }

    // ViewPager 어댑터
    inner class AlbumPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3 // 탭 개수

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TrackFragment()  // 수록곡 프래그먼트
                1 -> DetailFragment()
                2 -> VideoFragment()  // 동영상 프래그먼트
                else -> throw IllegalStateException("Invalid position")
            }
        }
    }
}

