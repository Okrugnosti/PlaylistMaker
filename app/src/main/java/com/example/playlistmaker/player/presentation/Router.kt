package com.example.playlistmaker.player.presentation

import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.search.SearchActivity

class Router(private val activity: AppCompatActivity) {
    fun getTrack(): Track =
        activity.intent.getSerializableExtra(SearchActivity.SEARCH_INPUT_KEY) as Track
}