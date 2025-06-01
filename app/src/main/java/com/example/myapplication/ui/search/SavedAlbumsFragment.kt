package com.example.myapplication.ui.search

//import AppDatabase
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentSavedAlbumsBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.data.firebase.LikeDao
import com.example.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.myapplication.data.entities.Album
import com.example.myapplication.ui.album.AlbumDao
import com.example.myapplication.ui.song.SongDatabase


//class SavedAlbumsFragment : Fragment() {
//
//    private var _binding: FragmentSavedAlbumsBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var adapter: SavedAlbumsAdapter
//    private lateinit var albumDao: AlbumDao
//
//    private val savedAlbums = mutableListOf<Album>() // 좋아요 앨범 리스트
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        // Fragment가 context에 붙었을 때 DB 초기화
//        albumDao = SongDatabase.getInstance(context)!!.albumDao()
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentSavedAlbumsBinding.inflate(inflater, container, false)
//
//        adapter = SavedAlbumsAdapter { album ->
//            // 클릭 시 상세 화면 이동
//            val bundle = Bundle().apply {
//                putParcelable("ALBUM_DATA", album)
//                putString("ALBUM_TITLE", album.title)
//                putInt(
//                    "IMAGE_RES_ID",
//                    requireContext().resources.getIdentifier(album.coverImg, "drawable", requireContext().packageName)
//                )
//            }
//            findNavController().navigate(R.id.action_savedAlbumsFragment_to_albumFragment, bundle)
//
//        }
//
//
//        binding.recyclerViewSavedAlbums.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerViewSavedAlbums.adapter = adapter
//
//        loadSavedAlbums()
//
//        return binding.root
//    }
//
////    fun loadSavedAlbums() {
////        lifecycleScope.launch(Dispatchers.IO) {
////            val likedAlbums = albumDao.getLikedAlbums()  // Room DB에서 좋아요 앨범 가져오기
////            savedAlbums.clear()
////            savedAlbums.addAll(likedAlbums)
////            withContext(Dispatchers.Main) {
////                adapter.submitList(savedAlbums.toList())
////            }
////        }
////    }
//
//    fun loadSavedAlbums() {
//        val userId = getCurrentUserId()
//        lifecycleScope.launch(Dispatchers.IO) {
//            val likedAlbumIds = SongDatabase.getInstance(requireContext())!!.likeDao().getLikedAlbumIds(userId)
//            val likedAlbums = albumDao.getAlbumsByIds(likedAlbumIds)
//
//            savedAlbums.clear()
//            savedAlbums.addAll(likedAlbums)
//
//            withContext(Dispatchers.Main) {
//                adapter.submitList(savedAlbums.toList())
//            }
//        }
//    }
//
//    private fun getCurrentUserId(): Int {
//        val sharedPreferences = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
//        return sharedPreferences.getInt("jwt", 0)
//    }
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//}


class SavedAlbumsFragment : Fragment() {

    private var _binding: FragmentSavedAlbumsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SavedAlbumsAdapter
    private lateinit var albumDao: AlbumDao
    private lateinit var likeDao: LikeDao

    private val savedAlbums = mutableListOf<Album>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        albumDao = SongDatabase.getInstance(context)!!.albumDao()
        likeDao = SongDatabase.getInstance(context)!!.likeDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedAlbumsBinding.inflate(inflater, container, false)

        adapter = SavedAlbumsAdapter(
            onAlbumClick = { album ->
                // 앨범 클릭 시 상세화면 이동
                val bundle = Bundle().apply {
                    putParcelable("ALBUM_DATA", album)
                    putString("ALBUM_TITLE", album.title)
                    putInt(
                        "IMAGE_RES_ID",
                        requireContext().resources.getIdentifier(album.coverImg, "drawable", requireContext().packageName)
                    )
                }
//                findNavController().navigate(R.id.action_savedAlbumsFragment_to_albumFragment, bundle)
                findNavController().navigate(R.id.action_navigation_search_to_albumFragment)

            },
            onAlbumImageClick = { album ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val userId = getCurrentUserId()

                    // Like 테이블에서 해당 좋아요 레코드 삭제 또는 isLike = false 처리
                    likeDao.deleteLike(userId, album.id)
                    // 또는
                    // val like = likeDao.getLike(userId, album.id)
                    // if (like != null) {
                    //     like.isLike = false
                    //     likeDao.update(like)
                    // }

                    // Album 테이블 isLike도 false로 업데이트
                    album.isLike = false
                    albumDao.update(album)

                    Log.d("SavedAlbumsFragment", "Album updated: ${album.title}, isLike = ${album.isLike}")

                    withContext(Dispatchers.Main) {
                        loadSavedAlbums()
                        Log.d("SavedAlbumsFragment", "loadSavedAlbums() called after update")
                    }
                }
            }



        )

        binding.recyclerViewSavedAlbums.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSavedAlbums.adapter = adapter

        loadSavedAlbums()

        return binding.root
    }

    fun loadSavedAlbums() {
        val userId = getCurrentUserId()
        lifecycleScope.launch(Dispatchers.IO) {
            val likedAlbumIds = SongDatabase.getInstance(requireContext())!!
                .likeDao()
                .getLikedAlbumIds(userId)

            val likedAlbums = albumDao.getAlbumsByIds(likedAlbumIds)

            Log.d("SavedAlbumsFragment", "likedAlbumIds size: ${likedAlbumIds.size}")
            Log.d("SavedAlbumsFragment", "likedAlbums size: ${likedAlbums.size}")
            likedAlbums.forEach {
                Log.d("SavedAlbumsFragment", "Liked album: ${it.title} isLike=${it.isLike}")
            }

            savedAlbums.clear()
            savedAlbums.addAll(likedAlbums)

            withContext(Dispatchers.Main) {
                adapter.submitList(savedAlbums.toList())
            }
        }
    }

    private fun getCurrentUserId(): Int {
        val sharedPreferences = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getInt("jwt", 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




