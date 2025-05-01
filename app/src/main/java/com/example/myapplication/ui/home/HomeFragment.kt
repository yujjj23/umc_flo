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
import com.example.myapplication.MusicPlayerManager
import com.example.myapplication.MainActivity
import android.widget.TextView
import android.widget.ImageView
import com.example.myapplication.AlbumData


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

        val btnPlayAlbum1 = view.findViewById<AppCompatImageButton>(R.id.btn_play_album1)
        val btnPlayAlbum2 = view.findViewById<AppCompatImageButton>(R.id.btn_play_album2)


//        btnPlayAlbum1.setOnClickListener {
//            playSongFromAlbum("Next Level", "aespa", R.drawable.img_album_exp3)
//        }
//
//        btnPlayAlbum2.setOnClickListener {
//            playSongFromAlbum("작은것들을 위한 시", "BTS", R.drawable.img_album_exp4)
//        }
// 버튼 클릭 시 곡을 재생하도록 수정
        btnPlayAlbum1.setOnClickListener {
            playSongFromAlbum(
                AlbumData.aespaAlbum[0], // "aenergy" 곡
                "aespa", // 아티스트
                R.drawable.img_album_exp3, // 앨범 이미지
                R.raw.bluedream_cheel // 음악 파일 (실제 파일 이름으로 변경해야 함)
            )
        }

        btnPlayAlbum2.setOnClickListener {
            playSongFromAlbum(
                AlbumData.btsAlbum[0], // "Intro : Persona" 곡
                "BTS", // 아티스트
                R.drawable.img_album_exp4, // 앨범 이미지
                R.raw.bluedream_cheel// 음악 파일 (실제 파일 이름으로 변경해야 함)
            )
        }



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


    private fun playSongFromAlbum(title: String, artist: String, albumResId: Int, resId: Int) {
        val context = requireContext()
        MusicPlayerManager.release()
        MusicPlayerManager.mediaPlayer = android.media.MediaPlayer.create(context, resId) // 이 부분 변경됨
        MusicPlayerManager.play()

        // MiniPlayer UI 업데이트
        val activity = activity as? MainActivity ?: return
        activity.findViewById<TextView>(R.id.tv_song_title).text = title
        activity.findViewById<TextView>(R.id.tv_artist_name).text = artist
        activity.findViewById<ImageView>(R.id.mini_btn_play).setImageResource(R.drawable.btn_miniplay_pause)
        activity.findViewById<View>(R.id.mini_player).visibility = View.VISIBLE
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
