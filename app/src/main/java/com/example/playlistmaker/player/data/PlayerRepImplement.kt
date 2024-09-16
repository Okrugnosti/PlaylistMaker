package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.model.PlayerState

//реализация интерфейса Плеера

class PlayerRepImplement(private val trackUrl: String) : PlayerRepository {
    override var playerState = PlayerState.STATE_DEFAULT

    private val musicPlayer: MediaPlayer = MediaPlayer()

    override fun definePlayer() {
        musicPlayer.apply {
            setDataSource(trackUrl)
            prepareAsync()
            playerState = PlayerState.STATE_PREPARED
            setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED
            }
        }
    }

    override fun startPlayer() {
        musicPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        musicPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun stopPlayer() = musicPlayer.release()

    override fun getPosition() = musicPlayer.currentPosition
}