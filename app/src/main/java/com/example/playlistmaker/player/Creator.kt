package com.example.playlistmaker.player

import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.player.data.PlayerRepImplement
import com.example.playlistmaker.player.domain.interactors.PlayerInteractorImplemented
import com.example.playlistmaker.player.presentation.PlayerPresenterImplemented
import com.example.playlistmaker.player.presentation.PlayerView
import com.example.playlistmaker.player.presentation.Router

object Creator {
    fun connectPresenter(
        playerView: PlayerView,
        activity: AppCompatActivity
    ): PlayerPresenterImplemented {
        val router = Router(activity)
        return PlayerPresenterImplemented(
            playerView = playerView,
            playerInteractor = PlayerInteractorImplemented(PlayerRepImplement(router.getTrack().previewUrl)),
            router = router
        )
    }
}