package com.example.playlistmaker.RecyclerV

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.trackАuthor)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    private val Url: ImageView = itemView.findViewById(R.id.trackIco)
    fun bind(bindTrack: Track) {
        trackName.text = bindTrack.trackName
        artistName.text = bindTrack.artistName
        trackTime.text = bindTrack.trackTime

        //загрузка картинки из Интернет + открыт доступ в манифест
        Glide.with(itemView)
            .load(bindTrack.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(3))
            .centerInside().into(Url)
    }
}