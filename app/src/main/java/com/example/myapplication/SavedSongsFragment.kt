package com.example.myapplication


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentSavedSongsBinding

class SavedSongsFragment : Fragment() {

    private var _binding: FragmentSavedSongsBinding? = null
    private val binding get() = _binding!!

    // 예시로 사용할 RecyclerView Adapter
    private lateinit var adapter: SavedSongsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // FragmentSavedSongsBinding을 인플레이트
        _binding = FragmentSavedSongsBinding.inflate(inflater, container, false)

        // RecyclerView 초기화 및 Adapter 설정
        binding.recyclerViewSavedSongs.layoutManager = LinearLayoutManager(context)
        adapter = SavedSongsAdapter()
        binding.recyclerViewSavedSongs.adapter = adapter

        val savedSongs = listOf("저장한 곡이 없습니다")
        adapter.submitList(savedSongs)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수를 방지하기 위해 바인딩 객체를 null로 설정
    }
}
