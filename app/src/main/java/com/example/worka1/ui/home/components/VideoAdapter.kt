package com.example.worka1.ui.home.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.worka1.R

class VideoAdapter(private val context: Context, private val videoList: List<VideoItem>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gifView: ImageView = itemView.findViewById(R.id.previewGif)
        val titleTextView: TextView = itemView.findViewById(R.id.previewTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_preview_item, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoItem = videoList[position]
        Glide.with(holder.itemView.context)
            .asGif()
            .load(videoItem.videoUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_add_to_cart_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.gifView)
        holder.titleTextView.text = videoItem.title
    }

    override fun getItemCount(): Int = videoList.size
}
