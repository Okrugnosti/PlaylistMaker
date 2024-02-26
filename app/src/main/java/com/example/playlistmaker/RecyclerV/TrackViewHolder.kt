package com.example.playlistmaker.RecyclerV

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale


//формирование по шаблону объекта
//class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class TrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.track_view, parentView, false)
) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.trackАuthor)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val url: ImageView = itemView.findViewById(R.id.trackIco)

    fun bind(bindTrack: Track) {
        trackName.text = bindTrack.trackName
        artistName.text = bindTrack.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(bindTrack.trackTime)

        //загрузка картинки из Интернет + открыт доступ в манифест
        Glide.with(itemView)
            .load(bindTrack.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerInside().transform(RoundedCorners(10)).into(url)
    }
}