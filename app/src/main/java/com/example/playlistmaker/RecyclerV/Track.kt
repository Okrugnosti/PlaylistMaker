package com.example.playlistmaker.RecyclerV


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    val trackId: String, //ID композиции
    val trackName: String, // Название композиции
    val artistName: String?, // Имя исполнителя
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
): Parcelable