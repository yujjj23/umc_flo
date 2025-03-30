package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

//class PanelAdapter(private val imageList: List<Pair<Int, Int>>) : RecyclerView.Adapter<PanelAdapter.PanelViewHolder>() {
//
//    inner class PanelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val imageView1: ImageView = itemView.findViewById(R.id.imageView1)
//        private val imageView2: ImageView = itemView.findViewById(R.id.imageView2)
//
//        fun bind(imageRes1: Int, imageRes2: Int) {
//            imageView1.setImageResource(imageRes1)
//            imageView2.setImageResource(imageRes2)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanelViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_panel, parent, false)
//        return PanelViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: PanelViewHolder, position: Int) {
//        val (imageRes1, imageRes2) = imageList[position]
//        holder.bind(imageRes1, imageRes2)
//    }
//
//    override fun getItemCount(): Int = imageList.size
//}

class PanelAdapter(private val imageList: List<Int>) : RecyclerView.Adapter<PanelAdapter.PanelViewHolder>() {

    inner class PanelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(imageRes: Int) {
            imageView.setImageResource(imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_panel, parent, false)
        return PanelViewHolder(view)
    }

    override fun onBindViewHolder(holder: PanelViewHolder, position: Int) {
        val imageRes = imageList[position]
        holder.bind(imageRes)
    }

    override fun getItemCount(): Int = imageList.size
}



