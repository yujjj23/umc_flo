package com.example.myapplication


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentMusicFilesBinding

class MusicFilesFragment : Fragment() {

    private var _binding: FragmentMusicFilesBinding? = null
    private val binding get() = _binding!!

    // 예시로 사용할 RecyclerView Adapter
    private lateinit var adapter: MusicFilesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // FragmentMusicFilesBinding을 인플레이트
        _binding = FragmentMusicFilesBinding.inflate(inflater, container, false)

        // RecyclerView 초기화 및 Adapter 설정
        binding.recyclerViewMusicFiles.layoutManager = LinearLayoutManager(context)
        adapter = MusicFilesAdapter()
        binding.recyclerViewMusicFiles.adapter = adapter

        // 예시로 데이터를 추가하는 부분 (여기서는 음악 파일의 이름 리스트를 사용)
        val musicFiles = listOf("Song 1", "Song 2", "Song 3")
        adapter.submitList(musicFiles)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수를 방지하기 위해 바인딩 객체를 null로 설정
    }
}
