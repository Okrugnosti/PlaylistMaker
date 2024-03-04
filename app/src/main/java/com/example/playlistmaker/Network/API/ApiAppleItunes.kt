package com.example.playlistmaker.Network.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


//создаем интерфейс по работе с API
interface ApiAppleItunes {
    @GET("/search?entity=song ")
    fun search(@Query("term") text: String) : Call<TrackResponse>

}