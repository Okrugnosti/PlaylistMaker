package com.example.playlistmaker.Network.API

import com.example.playlistmaker.RecyclerV.Track


//реализация класса с ответом по API

class TrackResponse(
    val resultCount: Int,
    val results: Array<Track>
)