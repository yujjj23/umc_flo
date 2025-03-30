package com.example.myapplication


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemSavedSongBinding

class SavedSongsAdapter : ListAdapter<String, SavedSongsAdapter.SavedSongViewHolder>(SavedSongDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSongViewHolder {
        val binding = ItemSavedSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedSongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedSongViewHolder, position: Int) {
        val song = getItem(position)
        holder.bind(song)
    }

    class SavedSongViewHolder(private val binding: ItemSavedSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: String) {
            binding.songTitle.text = song
        }
    }

    class SavedSongDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
