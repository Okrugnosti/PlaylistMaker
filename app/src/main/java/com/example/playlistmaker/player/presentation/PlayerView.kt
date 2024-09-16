package com.example.playlistmaker.player.presentation

import com.example.playlistmaker.player.domain.model.Track

interface PlayerView {
    fun getData(track: Track)
    fun goBack()
    fun setPauseImage()
    fun setStartImage()
    fun updateTimePlayed(timePlayed: String)
}