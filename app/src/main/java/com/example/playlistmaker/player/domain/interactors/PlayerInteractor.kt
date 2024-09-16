package com.example.playlistmaker.player.domain.interactors

import com.example.playlistmaker.player.domain.model.PlayerState

//
interface PlayerInteractor {
    fun getPlayerState(): PlayerState
    fun prepare()
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
}