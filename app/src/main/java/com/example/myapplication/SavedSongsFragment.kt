//package com.example.myapplication
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.myapplication.databinding.FragmentSavedSongsBinding
//import com.example.myapplication.model.Song  // Song 클래스 import 필요
//
//class SavedSongsFragment : Fragment() {
//
//    private var _binding: FragmentSavedSongsBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var adapter: SavedSongsAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentSavedSongsBinding.inflate(inflater, container, false)
//
//        // RecyclerView 초기화
//        binding.recyclerViewSavedSongs.layoutManager = LinearLayoutManager(context)
//        adapter = SavedSongsAdapter()
//        binding.recyclerViewSavedSongs.adapter = adapter
//
//        // ✅ 더미 데이터
//        val savedSongs = listOf(
//            Song("결론적으로", "SPARKY", R.drawable.img_album_exp),
//            Song("별거 없던 그 하루로", "임창정", R.drawable.img_album_exp2),
//            Song("선물 (White Day)", "코튼캔디(Cotton Candy)", R.drawable.img_album_exp3),
//            Song("Bad Boy (PREP Remix)", "Red Velvet(레드벨벳)", R.drawable.img_album_exp4),
//            Song("Always Me", "2am", R.drawable.img_album_exp),
//            Song("잘 가라니", "2am", R.drawable.img_album_exp2)
//        )
//
//        adapter.submitList(savedSongs)
//
//        return binding.root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}

package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentSavedSongsBinding
import com.example.myapplication.model.Song

class SavedSongsFragment : Fragment() {

    private var _binding: FragmentSavedSongsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SavedSongsAdapter
    private val savedSongs = mutableListOf(
        Song("결론적으로", "SPARKY", R.drawable.img_album_exp),
        Song("별거 없던 그 하루로", "임창정", R.drawable.img_album_exp2),
        Song("선물 (White Day)", "코튼캔디(Cotton Candy)", R.drawable.img_album_exp3),
        Song("Bad Boy (PREP Remix)", "Red Velvet(레드벨벳)", R.drawable.img_album_exp4),
        Song("Always Me", "2am", R.drawable.img_album_exp),
        Song("잘 가라니", "2am", R.drawable.img_album_exp2),
        Song("잘 가라니", "2am", R.drawable.img_album_exp2),
        Song("잘 가라니", "2am", R.drawable.img_album_exp2),
        Song("잘 가라니", "2am", R.drawable.img_album_exp2),
        Song("잘 가라니", "2am", R.drawable.img_album_exp2),
        Song("잘 가라니", "2am", R.drawable.img_album_exp2)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedSongsBinding.inflate(inflater, container, false)

        // 어댑터에 삭제 콜백 전달
        adapter = SavedSongsAdapter { songToDelete ->
            savedSongs.remove(songToDelete)
            adapter.submitList(savedSongs.toList()) // 새 리스트로 갱신
        }

        // RecyclerView 연결
        binding.recyclerViewSavedSongs.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSavedSongs.adapter = adapter

        adapter.submitList(savedSongs.toList()) // 초기 데이터 설정

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
