package com.example.myapplication.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.firebase.LikeManager
import com.example.myapplication.databinding.ItemSavedSongBinding
import com.example.myapplication.model.SongItem
import com.example.myapplication.ui.song.SongDatabase


class SavedSongsAdapter(
    private val onDeleteClick: (SongItem) -> Unit  // 🔸 삭제 콜백 추가
) : ListAdapter<SongItem, SavedSongsAdapter.SavedSongViewHolder>(SavedSongDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSongViewHolder {
        val binding = ItemSavedSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedSongViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: SavedSongViewHolder, position: Int) {
        val song = getItem(position)
        holder.bind(song)
    }

    class SavedSongViewHolder(
        private val binding: ItemSavedSongBinding,
        private val onDeleteClick: (SongItem) -> Unit  // 🔸 ViewHolder로 전달
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: SongItem) {
            binding.songTitle.text = song.title
            binding.songArtist.text = song.artist
            binding.albumImage.setImageResource(song.albumResId)

            // 🔸 스위치 상태 설정
            binding.switchToggle.setOnCheckedChangeListener(null)
            binding.switchToggle.isChecked = song.isChecked
            // 🔸 스위치 리스너 설정
            binding.switchToggle.setOnCheckedChangeListener { _, isChecked ->
                song.isChecked = isChecked
            }

//            binding.moreButton.setOnClickListener {
//                onDeleteClick(song)  // 🔸 삭제 콜백 호출
//            }
            binding.moreButton.setOnClickListener {
                // 1. Firebase에서 좋아요 취소
                val context = binding.root.context
                Thread {
                    val songDB = SongDatabase.getInstance(context)
                    val songDao = songDB?.songDao()
                    val matchedSong = songDao?.getSongs()?.find {
                        it.title == song.title && it.singer == song.artist
                    }

                    matchedSong?.let {
                        it.isLike = false

                        // Firebase에서 좋아요 취소
                        LikeManager.saveLikeToFirebase(it.id, false)

                        // RoomDB 업데이트
                        songDao.update(it)
                    }

                    // 2. UI에서 제거
                    (context as? FragmentActivity)?.runOnUiThread {
                        onDeleteClick(song)
                        Toast.makeText(context, "곡이 삭제되었습니다", Toast.LENGTH_SHORT).show()
                    }
                }.start()
            }
        }
    }

    class SavedSongDiffCallback : DiffUtil.ItemCallback<SongItem>() {
        override fun areItemsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return oldItem.title == newItem.title && oldItem.artist == newItem.artist
        }

        override fun areContentsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return oldItem == newItem
        }
    }
}
