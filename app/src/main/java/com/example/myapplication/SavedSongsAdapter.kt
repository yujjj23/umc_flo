package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemSavedSongBinding
import com.example.myapplication.model.Song

class SavedSongsAdapter(
    private val onDeleteClick: (Song) -> Unit  // 🔸 삭제 콜백 추가
) : ListAdapter<Song, SavedSongsAdapter.SavedSongViewHolder>(SavedSongDiffCallback()) {

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
        private val onDeleteClick: (Song) -> Unit  // 🔸 ViewHolder로 전달
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
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

            binding.moreButton.setOnClickListener {
                onDeleteClick(song)  // 🔸 삭제 콜백 호출
            }
        }
    }

    class SavedSongDiffCallback : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.title == newItem.title && oldItem.artist == newItem.artist
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }
    }
}
