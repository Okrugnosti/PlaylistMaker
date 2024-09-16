package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.model.PlayerState

//реализация интерфейса с базовым функционалам плеера

interface PlayerRepository {

    var playerState: PlayerState

    fun definePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun getPosition(): Int
}