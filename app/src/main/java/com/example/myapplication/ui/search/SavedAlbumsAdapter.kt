package com.example.myapplication.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.entities.Album
import com.example.myapplication.databinding.ItemSavedAlbumBinding


//class SavedAlbumsAdapter(
//    private val onAlbumClick: (Album) -> Unit
//) : ListAdapter<Album, SavedAlbumsAdapter.SavedAlbumViewHolder>(SavedAlbumDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAlbumViewHolder {
//        val binding = ItemSavedAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return SavedAlbumViewHolder(binding, onAlbumClick)
//    }
//
//    override fun onBindViewHolder(holder: SavedAlbumViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    class SavedAlbumViewHolder(
//        private val binding: ItemSavedAlbumBinding,
//        private val onAlbumClick: (Album) -> Unit
//    ) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(album: Album) {
//            binding.tvAlbumTitle.text = album.title ?: ""
//            binding.tvSingerTitle.text = album.singer ?: ""
//
//            val context = binding.root.context
//            val imageResId = context.resources.getIdentifier(album.coverImg, "drawable", context.packageName)
//            binding.rvAlbumImage.setImageResource(
//                if (imageResId != 0) imageResId else R.drawable.img_album_exp2
//            )
//
//            binding.root.setOnClickListener {
//                onAlbumClick(album)
//            }
//        }
//    }
//
//    class SavedAlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
//        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
//            return oldItem == newItem
//        }
//    }
//}


class SavedAlbumsAdapter(
    private val onAlbumClick: (Album) -> Unit,
    private val onAlbumImageClick: (Album) -> Unit // 이미지 클릭 콜백 추가
) : ListAdapter<Album, SavedAlbumsAdapter.SavedAlbumViewHolder>(SavedAlbumDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAlbumViewHolder {
        val binding = ItemSavedAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedAlbumViewHolder(binding, onAlbumClick, onAlbumImageClick)
    }

    override fun onBindViewHolder(holder: SavedAlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SavedAlbumViewHolder(
        private val binding: ItemSavedAlbumBinding,
        private val onAlbumClick: (Album) -> Unit,
        private val onAlbumImageClick: (Album) -> Unit // 이미지 클릭 콜백 추가
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.tvAlbumTitle.text = album.title ?: ""
            binding.tvSingerTitle.text = album.singer ?: ""

            val context = binding.root.context
            val imageResId = context.resources.getIdentifier(album.coverImg, "drawable", context.packageName)
            binding.rvAlbumImage.setImageResource(
                if (imageResId != 0) imageResId else R.drawable.img_album_exp2
            )

            // 전체 아이템 클릭 시 상세화면 이동
            binding.root.setOnClickListener {
                onAlbumClick(album)
            }

            // 이미지 클릭 시 좋아요 해제
            binding.rvAlbumImage.setOnClickListener {
                onAlbumImageClick(album)
            }
        }
    }

    class SavedAlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
}
