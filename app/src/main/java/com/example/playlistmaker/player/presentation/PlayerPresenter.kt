package com.example.playlistmaker.player.presentation

//интерфес для реализации функций
interface PlayerPresenter {
    fun onIvBackPressed()
    fun startPlayer()
    fun pausePlayer()
    fun onButtonClicked()
    fun convertTimeToString(duration: Int): String
    fun onViewPaus()
    fun onViewDestroy()
}