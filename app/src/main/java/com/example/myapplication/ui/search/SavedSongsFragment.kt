package com.example.myapplication.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.firebase.LikeManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSavedSongsBinding
import com.example.myapplication.model.SongItem
import com.example.myapplication.ui.song.SongDatabase

class
SavedSongsFragment : Fragment() {

    private var _binding: FragmentSavedSongsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SavedSongsAdapter
    private lateinit var songDB: SongDatabase
    private val savedSongs = mutableListOf<SongItem>() // SongItem 사용


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedSongsBinding.inflate(inflater, container, false)

        // DB 인스턴스 초기화
        songDB = SongDatabase.getInstance(requireContext())!!

        // 어댑터에 삭제 콜백 전달
        adapter = SavedSongsAdapter { songToDelete: SongItem ->
            savedSongs.remove(songToDelete)
            adapter.submitList(savedSongs.toList()) // 새 리스트로 갱신
        }

        // RecyclerView 연결
        binding.recyclerViewSavedSongs.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSavedSongs.adapter = adapter

        // 좋아요한 곡 불러오기
        loadSavedSongs()

        return binding.root
    }

//    fun loadSavedSongs() {
//        Thread {
//            // DB에서 좋아요한 곡만 가져오기
////            val likedSongs = songDB.songDao().getLikedSongs()
//            savedSongs.clear()
//
//            // Song을 SongItem으로 변환하여 추가
////            savedSongs.addAll(likedSongs.map {
////                // coverImg를 Int로 변환 (예시로, coverImg 값이 리소스 ID를 의미한다고 가정)
////                val albumResId = getAlbumResIdFromString(it.coverImg) // coverImg를 Int로 변환하는 메서드
////                SongItem(it.title, it.singer, albumResId)
////            })
//
//            requireActivity().runOnUiThread {
//                adapter.submitList(savedSongs.toList()) // RecyclerView에 데이터 업데이트
//            }
//        }.start()
//    }

    fun loadSavedSongs() {
        // 1단계: Firebase에서 좋아요된 ID 가져오기
        LikeManager.getLikedSongs { likedSongIds ->
            Thread {
                // 2단계: Room에서 ID로 곡 데이터 가져오기
                val songDB = SongDatabase.getInstance(requireContext())!!
                val likedSongs = likedSongIds.mapNotNull { id ->
                    songDB.songDao().getSongById(id)
                }

                // 3단계: Song → SongItem으로 변환
                savedSongs.clear()
                savedSongs.addAll(likedSongs.map {
                    val albumResId = getAlbumResIdFromString(it.coverImg)
                    SongItem(it.title, it.singer, albumResId)
                })

                // 4단계: UI 업데이트
                requireActivity().runOnUiThread {
                    adapter.submitList(savedSongs.toList())
                }
            }.start()
        }
    }



    //    // coverImg String을 Int로 변환하는 예시 함수
//    fun getAlbumResIdFromString(coverImg: String): Int {
//        return when (coverImg) {
//            "default_cover" -> R.drawable.img_album_exp2  // 예시로 리소스 ID 사용
//            else -> R.drawable.img_album_exp2 // 기본값 처리
//        }
//    }
    private fun getAlbumResIdFromString(coverImg: String): Int {
        val resId = requireContext().resources.getIdentifier(coverImg, "drawable", requireContext().packageName)
        return if (resId != 0) resId else R.drawable.img_album_exp2 // 기본 이미지
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun deleteAllLikedSongs() {
        LikeManager.clearAllLikes {
            Thread {
                songDB.songDao().updateAllSongsIsLikeFalse()
                savedSongs.clear()

                requireActivity().runOnUiThread {
                    adapter.submitList(savedSongs.toList())
                }
            }.start()
        }
    }

}

