package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.player.domain.model.Track
import com.google.gson.Gson

const val HISTORY_KEY = "search_history"

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun saveHist(historyList: ArrayList<Track>) {
        val json = Gson().toJson(historyList)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    fun loadHist(): Array<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun clearHist(){
        sharedPreferences.edit().remove(HISTORY_KEY).apply()
    }

}