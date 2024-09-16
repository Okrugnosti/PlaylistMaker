package com.example.playlistmaker.player.domain.model

//базовые состояния плейера
enum class PlayerState {
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED
}