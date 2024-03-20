package com.example.playlistmaker.Network.API

import com.example.playlistmaker.RecyclerV.Track

//реализация класса с ответом по API
//название переменных должно соответствовать названиям в API, либо использовать @SerializedName
class TrackResponse(
    val resultCount: Int,
    val results: Array<Track>
)
