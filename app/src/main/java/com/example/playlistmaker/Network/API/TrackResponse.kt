package com.example.playlistmaker.Network.API

import com.example.playlistmaker.RecyclerV.Track

//реализация класса с ответом по API
class TrackResponse(
    val resCount: Int,
    val resultations: Array<Track>
)