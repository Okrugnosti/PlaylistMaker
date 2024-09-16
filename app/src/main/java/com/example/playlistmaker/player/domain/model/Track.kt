package com.example.playlistmaker.player.domain.model

import com.google.gson.annotations.SerializedName

//описание данных трека
data class Track(
    val previewUrl: String,
    val trackId: String,
    val trackName: String,
    val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) : java.io.Serializable
