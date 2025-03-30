package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemBannerBinding

class BannerAdapter(private val context: Context) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    private val bannerImages = listOf(
        R.drawable.img_home_viewpager_exp,
        R.drawable.img_home_viewpager_exp2,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.imageView.setImageResource(bannerImages[position]) // 배너 이미지 설정
    }

    override fun getItemCount(): Int {
        return bannerImages.size
    }

    // ViewHolder 클래스
    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.bannerImageView)
    }
}
