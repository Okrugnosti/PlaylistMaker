package com.example.playlistmaker.RecyclerV

import com.google.gson.annotations.SerializedName

data class Track(
    val trackId: String, //ID композиции
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String // Ссылка на изображение обложки
)
