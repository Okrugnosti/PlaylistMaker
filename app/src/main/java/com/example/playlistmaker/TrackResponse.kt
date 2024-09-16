package com.example.playlistmaker

import com.example.playlistmaker.player.domain.model.Track

class TrackResponse(
    val resultCount: Int,
    val results: Array<Track>
)