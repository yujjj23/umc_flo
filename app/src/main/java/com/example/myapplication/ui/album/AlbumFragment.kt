package com.example.myapplication.ui.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.data.firebase.LikeDao
import com.example.myapplication.R
import com.example.myapplication.ui.search.SavedAlbumsFragment
import com.example.myapplication.ui.song.SongDatabase
import com.example.myapplication.data.entities.Album
import com.example.myapplication.data.entities.Like
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AlbumFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var btnBack: Button
    private lateinit var albumTitle: String
    private lateinit var albumSinger: String
    private var imageResId: Int = R.drawable.default_image
    private var isLiked: Boolean = false
    private lateinit var albumDao: AlbumDao
    private lateinit var album: Album

    private lateinit var likeButton: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var singerTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var likeDao: LikeDao


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_album, container, false)

        // UI 요소들 초기화
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)
        btnBack = view.findViewById(R.id.btn_back)
        likeButton = view.findViewById(R.id.iv_top_right_2)
        titleTextView = view.findViewById(R.id.tv_album_title)
        singerTextView = view.findViewById(R.id.tv_album_singer)
        imageView = view.findViewById(R.id.iv_center_image)

        // DB 초기화
        albumDao = SongDatabase.getInstance(requireContext())!!.albumDao()
        likeDao = SongDatabase.getInstance(requireContext())!!.likeDao()


        // arguments에서 Album 객체 가져오기
        album = arguments?.getParcelable("ALBUM_DATA") ?: run {
            Toast.makeText(requireContext(), "앨범 정보가 없습니다.", Toast.LENGTH_SHORT).show()
            return view
        }

        albumTitle = album.title ?: "앨범 정보 없음"
        albumSinger = album.singer ?: "가수 정보 없음"

        // ViewPager 어댑터 설정
        val adapter = AlbumPagerAdapter(this)
        viewPager.adapter = adapter

        // DB 초기화
// 안전하게 non-null 반환을 위해 !! 사용하거나, nullable 처리 가능
        val likeDao = SongDatabase.getInstance(requireContext())!!.likeDao()




        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "수록곡"
                1 -> tab.text = "상세정보"
                2 -> tab.text = "동영상"
            }
        }.attach()

        // 우선 arguments에서 받은 데이터로 UI 설정 (이미지, 제목, 가수명)
        imageResId = resources.getIdentifier(album.coverImg, "drawable", requireContext().packageName)
        imageView.setImageResource(imageResId)
        titleTextView.text = albumTitle
        singerTextView.text = albumSinger

        // --- 여기서부터 DB에서 최신 데이터 불러오기 (비동기) ---
        lifecycleScope.launch {
            val latestAlbum = withContext(Dispatchers.IO) {
                albumDao.getAlbumById(album.id)
            }
            if (latestAlbum != null) {
                album = latestAlbum
                isLiked = album.isLike

                // UI는 메인스레드에서 업데이트
                requireActivity().runOnUiThread {
                    updateLikeButtonUI(likeButton)
                    // 필요하면 다른 UI도 갱신
                    titleTextView.text = album.title ?: "앨범 정보 없음"
                    singerTextView.text = album.singer ?: "가수 정보 없음"
                    val resId = resources.getIdentifier(album.coverImg, "drawable", requireContext().packageName)
                    imageView.setImageResource(resId)
                }
            }
        }


        // ✅ 1. 현재 로그인 유저의 좋아요 상태만 불러오기
        lifecycleScope.launch {
            val userId = getCurrentUserId()

            val userLike = withContext(Dispatchers.IO) {
                likeDao.getLike(userId, album.id)
            }

            isLiked = userLike?.isLike ?: false // 기본 false 처리
            updateLikeButtonUI(likeButton)
        }


        // 좋아요 버튼 클릭 이벤트
        likeButton.setOnClickListener {
            isLiked = !isLiked
            updateLikeButtonUI(likeButton)

            lifecycleScope.launch(Dispatchers.IO) {
                val userId = getCurrentUserId()

                // 🎯 1번 방법: user와 album 존재 확인
                val user = SongDatabase.getInstance(requireContext())!!.userDao().getUserById(userId)
                val albumInDb = albumDao.getAlbumById(album.id)

                if (user != null && albumInDb != null) {
                    val like = Like(
                        userId = userId,
                        albumId = album.id,
                        isLike = isLiked
                    )

                    // Like 테이블에 삽입
                    likeDao.insertLike(like)

                    // Album 테이블 isLike 상태 업데이트
                    album.isLike = isLiked
                    albumDao.updateAlbum(album)

                    Log.d("AlbumFragment", "좋아요 상태 DB에 반영됨")
                } else {
                    Log.e("AlbumFragment", "User 또는 Album이 존재하지 않아서 Like를 저장할 수 없음")
                }
            }


            // 저장 앨범 화면이 열려 있을 경우 갱신
            val savedAlbumsFragment = parentFragmentManager.findFragmentByTag("SavedAlbumsFragment") as? SavedAlbumsFragment
            savedAlbumsFragment?.let {
                if (it.isAdded) it.loadSavedAlbums()
            }
        }




        // 뒤로 가기 버튼
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_albumFragment_to_homeFragment)
        }

        // 물리적 뒤로가기 버튼 처리
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_albumFragment_to_homeFragment)
        }

        return view
    }

    private fun updateLikeButtonUI(button: ImageView) {
        if (isLiked) {
            button.setImageResource(R.drawable.ic_my_like_on)
        } else {
            button.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun getCurrentUserId(): Int {
        val sharedPreferences = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getInt("jwt", 0)
    }

    inner class AlbumPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TrackFragment()
                1 -> DetailFragment()
                2 -> VideoFragment()
                else -> throw IllegalStateException("Invalid position")
            }
        }
    }
}




