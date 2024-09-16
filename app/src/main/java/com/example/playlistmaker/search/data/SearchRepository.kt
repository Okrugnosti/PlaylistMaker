package com.example.playlistmaker.search.data

import com.example.playlistmaker.ItunesAppleApi
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.TrackResponse
import com.example.playlistmaker.search.SearchActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository(private val api: ItunesAppleApi) {

    fun loadTracks(queryInput: String, onSuccess: (Array<Track>) -> Unit, onError: () -> Unit) {
        api.search(queryInput)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == SearchActivity.SUCCESS) {
                        onSuccess.invoke(response.body()?.results!!)
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    onError.invoke()
                }

            })
    }
}