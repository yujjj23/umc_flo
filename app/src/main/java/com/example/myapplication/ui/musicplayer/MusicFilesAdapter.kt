package com.example.myapplication.ui.musicplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemMusicFileBinding

class MusicFilesAdapter : ListAdapter<String, MusicFilesAdapter.MusicFileViewHolder>(
    MusicFileDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicFileViewHolder {
        val binding = ItemMusicFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicFileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicFileViewHolder, position: Int) {
        val musicFile = getItem(position)
        holder.bind(musicFile)
    }

    class MusicFileViewHolder(private val binding: ItemMusicFileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(musicFile: String) {
            binding.musicFileName.text = musicFile
        }
    }

    class MusicFileDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
