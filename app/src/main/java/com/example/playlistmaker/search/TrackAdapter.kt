package com.example.playlistmaker.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.player.domain.model.Track

class TrackAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var recentTracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun getItemCount() = recentTracks.size
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(recentTracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(recentTracks[position]) }
    }
}

fun interface TrackClickListener {
    fun onTrackClick(track: Track)
}