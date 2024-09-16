package com.example.playlistmaker.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.ItunesAppleApi
import com.example.playlistmaker.R
import com.example.playlistmaker.TrackResponse
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.data.HISTORY_KEY
import com.example.playlistmaker.search.data.SearchHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    // Views initialization and variables declarations
    private lateinit var queryInput: EditText
    private lateinit var back: View
    private lateinit var ivClearInputText: ImageView
    private lateinit var clearHistoryButton: Button
    private lateinit var refresh: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var rvTrack: RecyclerView
    private lateinit var rvHistoryTrack: RecyclerView

    private lateinit var notFound: LinearLayout
    private lateinit var netIssue: LinearLayout
    private lateinit var searchHistoryFragment: LinearLayout

    private lateinit var searchHistory: SearchHistory

    private val tracks = ArrayList<Track>()
    private val historyTracks = ArrayList<Track>()

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }

    companion object {
        const val SEARCH_INPUT_KEY = "SEARCH_INPUT"
        const val SUCCESS = 200
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    enum class SearchStatus {
        PROGRESS,
        CONNECTION_ERROR,
        EMPTY_SEARCH,
        SUCCESS,
        HISTORY,
        ALL_GONE
    }

    //Retrofit set up
    private val itunesUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesAppleApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchHistory = SearchHistory(getSharedPreferences(HISTORY_KEY, MODE_PRIVATE))

        initViews()
        setupListeners()
        setupAdapters()
    }

    private fun initViews() {
        back = findViewById(R.id.back)
        ivClearInputText = findViewById(R.id.clear_text)
        queryInput = findViewById(R.id.search_input)
        refresh = findViewById(R.id.refreshButton)
        clearHistoryButton = findViewById(R.id.delete_history_button)
        progressBar = findViewById(R.id.progressBar)
        rvTrack = findViewById(R.id.recyclerSearch)
        rvHistoryTrack = findViewById(R.id.recycler_history)
        notFound = findViewById(R.id.error_nothing_found)
        netIssue = findViewById(R.id.error_no_connection)
        searchHistoryFragment = findViewById(R.id.search_history_fragment)
    }

    private fun setupAdapters() {
        historyTracks.addAll(searchHistory.loadHist())

        if (historyTracks.isNotEmpty())
            searchHistoryFragment.visibility = View.VISIBLE

        val trackAdapter = TrackAdapter {
            if (clickDebounce()) {
                addToRecentHistoryList(it)
                transferDataToPlayerActivity(it)
            }
        }
        trackAdapter.recentTracks = tracks
        rvTrack.adapter = trackAdapter

        val historyTrackAdapter = TrackAdapter {
            if (clickDebounce()) {
                transferDataToPlayerActivity(it)
            }
        }
        historyTrackAdapter.recentTracks = historyTracks
        rvHistoryTrack.adapter = historyTrackAdapter
    }

    private fun transferDataToPlayerActivity(track: Track) {
        val intent: Intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(SEARCH_INPUT_KEY, track)
        startActivity(intent)
    }

    private fun setupListeners() {
        back.setOnClickListener { finish() }
        refresh.setOnClickListener { search() }

        ivClearInputText.setOnClickListener {
            queryInput.text = null
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(queryInput.windowToken, 0)
            rvTrack.visibility = View.GONE
            if (historyTracks.isEmpty()) {
                setStatus(SearchStatus.ALL_GONE)
            } else setStatus(SearchStatus.HISTORY)
        }

        queryInput.apply { requestFocus() }
        queryInput.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                ivClearInputText.visibility = View.GONE
                if (historyTracks.isEmpty()) {
                    setStatus(SearchStatus.ALL_GONE)
                } else setStatus(SearchStatus.HISTORY)
            } else {
                searchDebounce()
                ivClearInputText.visibility = View.VISIBLE
                searchHistoryFragment.visibility = View.GONE
            }
        }
        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (queryInput.text.isNotEmpty())
                    search()
                else {
                    if (historyTracks.isEmpty()) {
                        setStatus(SearchStatus.ALL_GONE)
                    } else setStatus(SearchStatus.HISTORY)
                }
                true
            }
            false
        }
        queryInput.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryFragment.visibility =
                if (hasFocus && queryInput.text.isEmpty()) View.VISIBLE else View.GONE
        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clearHist()
            searchHistoryFragment.visibility = View.GONE
            historyTracks.clear()
            rvHistoryTrack.adapter?.notifyDataSetChanged()
        }
    }

    override fun onStop() {
        super.onStop()
        searchHistory.saveHist(historyTracks)
    }

    private fun search() {
        if (queryInput.text.isNotEmpty()) {
            setStatus(SearchStatus.PROGRESS)
            itunesService.search(queryInput.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {

                        if (response.code() == SUCCESS) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                rvTrack.adapter?.notifyDataSetChanged()
                                setStatus(SearchStatus.SUCCESS)
                            }
                            if (tracks.isEmpty()) {
                                setStatus(SearchStatus.EMPTY_SEARCH)
                            }
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        setStatus(SearchStatus.CONNECTION_ERROR)
                    }

                })
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun setStatus(status: SearchStatus) {
        when (status) {
            SearchStatus.PROGRESS -> {
                progressBar.visibility = View.VISIBLE
                searchHistoryFragment.visibility = View.GONE
                netIssue.visibility = View.GONE
                notFound.visibility = View.GONE
                rvTrack.visibility = View.GONE
            }

            SearchStatus.CONNECTION_ERROR -> {
                netIssue.visibility = View.VISIBLE
                searchHistoryFragment.visibility = View.GONE
                rvTrack.visibility = View.GONE
                notFound.visibility = View.GONE
                progressBar.visibility = View.GONE
            }

            SearchStatus.EMPTY_SEARCH -> {
                notFound.visibility = View.VISIBLE
                searchHistoryFragment.visibility = View.GONE
                netIssue.visibility = View.GONE
                rvTrack.visibility = View.GONE
                progressBar.visibility = View.GONE
            }

            SearchStatus.SUCCESS -> {
                rvTrack.visibility = View.VISIBLE
                searchHistoryFragment.visibility = View.GONE
                netIssue.visibility = View.GONE
                notFound.visibility = View.GONE
                progressBar.visibility = View.GONE
            }

            SearchStatus.HISTORY -> {
                searchHistoryFragment.visibility = View.VISIBLE
                netIssue.visibility = View.GONE
                notFound.visibility = View.GONE
                rvTrack.visibility = View.GONE
                progressBar.visibility = View.GONE
            }

            SearchStatus.ALL_GONE -> {
                searchHistoryFragment.visibility = View.GONE
                netIssue.visibility = View.GONE
                notFound.visibility = View.GONE
                rvTrack.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun addToRecentHistoryList(track: Track) {
        for (index in historyTracks.indices) {
            if (historyTracks[index].trackId == track.trackId) {
                historyTracks.removeAt(index)
                historyTracks.add(0, track)
                rvHistoryTrack.adapter?.notifyItemMoved(index, 0)
                return
            }
        }
        if (historyTracks.size < 10) {
            historyTracks.add(0, track)
            rvHistoryTrack.adapter?.notifyItemInserted(0)
            rvHistoryTrack.adapter?.notifyItemRangeChanged(
                0,
                historyTracks.size
            )
        } else {
            historyTracks.removeAt(9)
            rvHistoryTrack.adapter?.notifyItemRemoved(0)
            rvHistoryTrack.adapter?.notifyItemRangeChanged(
                9,
                historyTracks.size
            )
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchText = queryInput.text.toString()
        outState.putString(SEARCH_INPUT_KEY, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedText = savedInstanceState.getString(SEARCH_INPUT_KEY)
        queryInput.setText(savedText)
    }


}