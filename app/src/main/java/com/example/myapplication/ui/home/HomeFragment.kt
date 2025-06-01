package com.example.myapplication.ui.home

//import AppDatabase
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.ui.main.BannerAdapter
import com.example.myapplication.ui.main.PanelAdapter
import com.example.myapplication.R
import me.relex.circleindicator.CircleIndicator3
import com.example.myapplication.ui.main.MainActivity
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.entities.Album
import com.example.myapplication.ui.album.AlbumDao
import com.example.myapplication.ui.song.SongDao
import com.example.myapplication.ui.song.SongDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicator: CircleIndicator3
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var adapter: BannerAdapter
    private lateinit var panelAdapter: PanelAdapter

    private lateinit var albumDao: AlbumDao
//    private lateinit var albumDatabase: AppDatabase
    lateinit var songDatabase: SongDatabase
    lateinit var songDao: SongDao

//    private lateinit var albumDatabase: SongDatabase

    private lateinit var btnNextSong: AppCompatImageButton // "다음 곡" 버튼 선언

    private val panelImages = listOf(
        R.drawable.img_first_album_default,
        R.drawable.discovery_banner_aos,
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

//        albumDatabase = DatabaseProvider.getDatabase(requireContext())
        songDatabase = SongDatabase.getInstance(requireContext())!!

        albumDao = songDatabase.albumDao()
//        songDatabase = SongDatabase.getInstance(requireContext())!!  // ✅ 이 줄 꼭 추가!
        songDao = songDatabase.songDao()

        val btnGoToAlbum = view.findViewById<AppCompatImageButton>(R.id.btn_go_to_album)
        val btnGoToAlbum2 = view.findViewById<AppCompatImageButton>(R.id.btn_go_to_album2)

        val panelViewPager = view.findViewById<ViewPager2>(R.id.panelViewPager)
        val panelIndicator = view.findViewById<CircleIndicator3>(R.id.panelIndicator)

        val btnPlayAlbum1 = view.findViewById<AppCompatImageButton>(R.id.btn_play_album1)
        val btnPlayAlbum2 = view.findViewById<AppCompatImageButton>(R.id.btn_play_album2)


        // ✅ 수정된 앨범 전체 재생
//        btnPlayAlbum1.setOnClickListener {
//            playFirstSongFromAlbum(1) // albumId = 1
//        }
//
//        btnPlayAlbum2.setOnClickListener {
//            playFirstSongFromAlbum(2) // albumId = 2
//        }

        btnPlayAlbum1.setOnClickListener {
            val albumId = 1
            loadSongsFromAlbum(albumId)
        }

        btnPlayAlbum2.setOnClickListener {
            val albumId = 2
            loadSongsFromAlbum(albumId)
        }

        panelAdapter = PanelAdapter(panelImages)
        panelViewPager.adapter = panelAdapter
        panelIndicator.setViewPager(panelViewPager)

        viewPager = view.findViewById(R.id.viewPagerBanner)
        indicator = view.findViewById(R.id.indicatorBanner)
        adapter = BannerAdapter(requireContext())
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val nextPanelItem = (panelViewPager.currentItem + 1) % panelAdapter.itemCount
                panelViewPager.setCurrentItem(nextPanelItem, true)

                val nextBannerItem = (viewPager.currentItem + 1) % adapter.itemCount
                viewPager.setCurrentItem(nextBannerItem, true)

                handler.postDelayed(this, 5000)
            }
        }
        handler.postDelayed(runnable, 5000)

//        btnGoToAlbum.setOnClickListener {
//            navigateToAlbum(btnGoToAlbum)
//        }
//
//        btnGoToAlbum2.setOnClickListener {
//            navigateToAlbum(btnGoToAlbum2)
//        }

        loadAlbumData(view)

        return view
    }


    private fun loadAlbumData(view: View) {
        lifecycleScope.launch {
            // 백그라운드 스레드에서 DB 호출
            val albums = withContext(Dispatchers.IO) {
                albumDao.getAllAlbums()
            }

            if (albums.isNotEmpty()) {
                val album1 = albums[0]
                view.findViewById<AppCompatImageButton>(R.id.btn_go_to_album).apply {
                    setImageResource(resources.getIdentifier(album1.coverImg, "drawable", context?.packageName))
                    setOnClickListener {
                        navigateToAlbum(album1)
                    }
                }
                view.findViewById<TextView>(R.id.text_title1).text = album1.title
                view.findViewById<TextView>(R.id.text_artist1).text = album1.singer
            }

            if (albums.size > 1) {
                val album2 = albums[1]
                view.findViewById<AppCompatImageButton>(R.id.btn_go_to_album2).apply {
                    setImageResource(resources.getIdentifier(album2.coverImg, "drawable", context?.packageName))
                    setOnClickListener {
                        navigateToAlbum(album2)
                    }
                }
                view.findViewById<TextView>(R.id.text_title2).text = album2.title
                view.findViewById<TextView>(R.id.text_artist2).text = album2.singer
            }
        }
    }





    private fun loadSongsFromAlbum(albumId: Int) {
        lifecycleScope.launch {
            val songs = songDao.getSongsByAlbum(albumId)
            if (songs.isNotEmpty()) {
                // MainActivity로 곡 정보를 전달하여 첫 곡 재생
                val activity = activity as? MainActivity
                activity?.let {
                    val firstSong = songs[0]
                    it.setSongs(songs)
                    it.setCurrentSongIndex(0)
                    it.playSongFromOutside(firstSong)
                }
            } else {
                Toast.makeText(requireContext(), "해당 앨범에 수록된 곡이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun navigateToAlbum(button: AppCompatImageButton) {
//        val imageResId = getImageResourceId(button)
//        val albumTitle = getAlbumTitle(imageResId)
//
//        val bundle = Bundle().apply {
//            putString("ALBUM_TITLE", albumTitle)
//            putInt("IMAGE_RES_ID", imageResId)
//        }
//        findNavController().navigate(R.id.action_homeFragment_to_albumFragment, bundle)
//    }

    private fun navigateToAlbum(album: Album) {
        val bundle = Bundle().apply {
            putParcelable("ALBUM_DATA", album)
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
    override fun onResume() {
        super.onResume()
        loadAlbumData(requireView()) // 앨범 정보를 다시 로드하여 최신 좋아요 상태 반영
    }

}


