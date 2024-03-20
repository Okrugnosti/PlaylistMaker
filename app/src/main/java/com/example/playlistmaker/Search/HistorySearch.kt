package com.example.playlistmaker.Search

import android.content.SharedPreferences
import com.example.playlistmaker.RecyclerV.Track
import com.google.gson.Gson

const val HISTORY_KEY = "search_history"

//Реализация истории поиска
class HistorySearch(private val sharedPreferences: SharedPreferences) {

    //сохранение в sharedPreferences
    fun saveHistory(historyList: ArrayList<Track>) {
        val json = Gson().toJson(historyList)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }
    //загрузка в sharedPreferences
    fun loadHistory(): Array<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    //удаление в sharedPreferences
    fun clearHistory(){
        sharedPreferences.edit().remove(HISTORY_KEY).apply()
    }

}