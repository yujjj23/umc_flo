package com.example.myapplication.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.BannerAdapter
import com.example.myapplication.PanelAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import me.relex.circleindicator.CircleIndicator3

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicator: CircleIndicator3
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var adapter: BannerAdapter
    private lateinit var panelAdapter: PanelAdapter

    // 이미지 리스트 생성
    private val panelImages = listOf(
        R.drawable.img_first_album_default, // 첫 번째 이미지
        R.drawable.discovery_banner_aos, // 두 번째 이미지
        // 더 많은 이미지 추가 가능
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnGoToAlbum = view.findViewById<AppCompatImageButton>(R.id.btn_go_to_album)
        val btnGoToAlbum2 = view.findViewById<AppCompatImageButton>(R.id.btn_go_to_album2)

        // Panel ViewPager 설정
        val panelViewPager = view.findViewById<ViewPager2>(R.id.panelViewPager)
        val panelIndicator = view.findViewById<CircleIndicator3>(R.id.panelIndicator)


        // PanelAdapter 설정
        panelAdapter = PanelAdapter(panelImages)
        panelViewPager.adapter = panelAdapter
        panelIndicator.setViewPager(panelViewPager)

        // Banner ViewPager 설정
        viewPager = view.findViewById(R.id.viewPagerBanner)
        indicator = view.findViewById(R.id.indicatorBanner)
        adapter = BannerAdapter(requireContext())
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)

        // 자동 슬라이드 설정
        handler = Handler(Looper.getMainLooper())

        // 하나의 Runnable로 두 개의 ViewPager2 자동 슬라이드 처리
        runnable = Runnable {
            // Panel 자동 슬라이드
            val nextPanelItem = (panelViewPager.currentItem + 1) % panelAdapter.itemCount
            panelViewPager.setCurrentItem(nextPanelItem, true)

            // Banner 자동 슬라이드
            val nextBannerItem = (viewPager.currentItem + 1) % adapter.itemCount
            viewPager.setCurrentItem(nextBannerItem, true)

            // 5초마다 슬라이드
            handler.postDelayed(runnable, 5000)
        }

        // 최초 5초 뒤에 첫 번째 실행
        handler.postDelayed(runnable, 5000)

        // 첫 번째 ImageButton 클릭 이벤트
        btnGoToAlbum.setOnClickListener {
            navigateToAlbum(btnGoToAlbum)
        }

        // 두 번째 ImageButton 클릭 이벤트
        btnGoToAlbum2.setOnClickListener {
            navigateToAlbum(btnGoToAlbum2)
        }

        return view
    }

    private fun navigateToAlbum(button: AppCompatImageButton) {
        val imageResId = getImageResourceId(button)
        val albumTitle = getAlbumTitle(imageResId)

        val bundle = Bundle().apply {
            putString("ALBUM_TITLE", albumTitle)
            putInt("IMAGE_RES_ID", imageResId)
        }
        findNavController().navigate(R.id.action_homeFragment_to_albumFragment, bundle)
    }

    private fun getImageResourceId(button: AppCompatImageButton): Int {
        return when (button.drawable.constantState) {
            ContextCompat.getDrawable(requireContext(), R.drawable.img_album_exp4)?.constantState -> R.drawable.img_album_exp4
            ContextCompat.getDrawable(requireContext(), R.drawable.img_album_exp2)?.constantState -> R.drawable.img_album_exp2
            ContextCompat.getDrawable(requireContext(), R.drawable.img_album_exp3)?.constantState -> R.drawable.img_album_exp3
            else -> R.drawable.default_image
        }
    }

    private fun getAlbumTitle(imageResId: Int): String {
        return when (imageResId) {
            R.drawable.img_album_exp4 -> "bts 5th album \n        bts\n   2025.03.29"
            R.drawable.img_album_exp2 -> "iu 5th album\n       iu\n   2025.03.29"
            R.drawable.img_album_exp3 -> "aespa 5th album\n       aespa\n   2025.03.29"
            else -> "앨범 정보 없음"
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}
