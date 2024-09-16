package com.example.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.player.domain.interactors.PlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerPresenterImplemented(
    val playerView: PlayerView,
    val playerInteractor: PlayerInteractor,
    val router: Router
) : PlayerPresenter {

    private val handler = Handler(Looper.getMainLooper())

    init {
        playerView.getData(router.getTrack())
        playerInteractor.prepare()
    }

    override fun onIvBackPressed() {
        playerView.goBack()
    }

    override fun startPlayer() {
        playerInteractor.start()
        playerView.setPauseImage()

        handler.postDelayed(object : Runnable {
            override fun run() {
                playerView.updateTimePlayed(convertTimeToString(playerInteractor.getCurrentPosition()))
                val state = playerInteractor.getPlayerState()
                if (state == PlayerState.STATE_PREPARED) {
                    playerView.setStartImage()
                    playerView.updateTimePlayed("00:00")
                    handler.removeCallbacksAndMessages(null)
                }
                handler.postDelayed(this, DELAY)
            }
        }, DELAY)
    }

    override fun pausePlayer() {
        playerInteractor.pause()
        playerView.setStartImage()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onButtonClicked() {
        when (playerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> startPlayer()
            PlayerState.STATE_DEFAULT -> {
                playerInteractor.prepare()
                startPlayer()
            }
        }
    }

    override fun convertTimeToString(duration: Int): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)

    override fun onViewPaus() {
        playerInteractor.pause()
        playerView.setStartImage()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onViewDestroy() {
        playerInteractor.release()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val DELAY = 1000L
    }
}