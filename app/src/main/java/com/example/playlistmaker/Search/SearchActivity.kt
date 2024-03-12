package com.example.playlistmaker.Search

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.playlistmaker.Network.API.TrackResponse
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Network.API.ApiAppleItunes
import com.example.playlistmaker.R
import com.example.playlistmaker.RecyclerV.Track
import com.example.playlistmaker.RecyclerV.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    //константы
    companion object {
        const val SEARCH_INPUT = "SEARCH_INPUT"
        const val SUCCESS = 200
        const val LISTSIZE = 10
    }

    //ответы от сервера API
    enum class SearchStatus {
        CONNECTION_ERROR,
        EMPTY_SEARCH,
        SUCCESS,
        HISTORY,
        ALL_GONE
    }

    // Элементы View
    private lateinit var enterTextButton: EditText
    private lateinit var backButton: View
    private lateinit var clearButton: ImageView
    private lateinit var clearHistoryButton: Button
    private lateinit var refresh: Button

    private lateinit var recyclerTrack: RecyclerView
    private lateinit var recyclerHistoryTrack: RecyclerView

    private lateinit var errNotFound: LinearLayout
    private lateinit var errNoConnect: LinearLayout
    private lateinit var searchHistoryFragment: LinearLayout

    private lateinit var historySearch: HistorySearch

    private val tracks = ArrayList<Track>()
    private val historyTracks = ArrayList<Track>() //массив треков

    //объекты API и Retrofit
    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ApiAppleItunes::class.java)

    //запуск Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        historySearch = HistorySearch(getSharedPreferences(HISTORY_KEY, MODE_PRIVATE))

        initViews() //инициализация элементов View
        setupAdapters() //создание адаптеров
        setupListeners() //реализация кнопок

    }

    //привязка элементов View
    private fun initViews() {
        backButton = findViewById(R.id.search_vector) //кнопка Назад
        clearButton = findViewById(R.id.clearButtonSearch) //кнопка очистки текста
        enterTextButton = findViewById(R.id.editTextSearch) //поле ввода текста на клавиатуре
        refresh = findViewById(R.id.refreshButton) //обновление запроса
        clearHistoryButton = findViewById(R.id.delete_history_button) //кнопка очистки истории поиска
        recyclerTrack = findViewById(R.id.recyclerView) //ссылка на форму "списка треков"
        recyclerHistoryTrack = findViewById(R.id.recycler_history) //ссылка на форму "списка треков"
        errNotFound = findViewById(R.id.err_not_found) //запуск формы нет данных
        errNoConnect = findViewById(R.id.err_no_connection) //запуск формы нет связи
        searchHistoryFragment = findViewById(R.id.search_history_fragment)
    }

    private fun setupAdapters() {
        historyTracks.addAll(historySearch.loadHistory())

        if (historyTracks.isNotEmpty())
            searchHistoryFragment.visibility = View.VISIBLE

        val trackAdapter = TrackAdapter {
            addToRecentHistoryList(it)

            //переход на трек и удаление "тоста"
            Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show()
        }
        trackAdapter.recentTracks = tracks
        recyclerTrack.adapter = trackAdapter

        val historyTrackAdapter = TrackAdapter {
            //переход на трек
        }

        historyTrackAdapter.recentTracks = historyTracks
        recyclerHistoryTrack.adapter = historyTrackAdapter
    }

    private fun setupListeners() {
        //нажатие на кнопку "назад"
        backButton.setOnClickListener { finish() }

        //надажтие на кнопку "обновить"
        refresh.setOnClickListener { search() }

        //нажатие на кнопку "очистить"
        clearButton.setOnClickListener {
            enterTextButton.text = null
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(enterTextButton.windowToken, 0)

            recyclerTrack.visibility = View.GONE
            if (historyTracks.isEmpty()) {
                setStatus(SearchStatus.ALL_GONE)

            } else setStatus(SearchStatus.HISTORY)
            Log.d("recyclerTrack.visibility", "On")
        }

        //в historyTracks живет список треков. как найти клики по списку
        //recyclerTrack визуализирует список треков
        //посмотреть события, при нажатии на иконки в списке поиска searchHistoryFragment.visibility = View.VISIBLE
        //где происходит добавление трека в ИсториюПоиска

        //ввод текста на клавиатуре
        enterTextButton.apply { requestFocus() }
        enterTextButton.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                clearButton.visibility = View.GONE
                if (historyTracks.isEmpty()) {
                    setStatus(SearchStatus.ALL_GONE)
                } else setStatus(SearchStatus.HISTORY)
            } else {
                clearButton.visibility = View.VISIBLE
                searchHistoryFragment.visibility = View.GONE
            }
        }
        enterTextButton.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (enterTextButton.text.isNotEmpty())
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
        enterTextButton.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryFragment.visibility =
                if (hasFocus && enterTextButton.text.isEmpty()) View.VISIBLE else View.GONE
            Log.d("enterTextButton.setOnFocusChangeListener", "On")
        }

        //очистка списка треков
        clearHistoryButton.setOnClickListener {
            historySearch.clearHistory() //экземпляр объекта HistorySearch
            searchHistoryFragment.visibility = View.GONE
            historyTracks.clear() //historyTracks - список треков.
            recyclerHistoryTrack.adapter?.notifyDataSetChanged()
            Log.d("clearHistoryButton.setOnClickListener", "On")
        }
    }

    //сохранение истории поиска
    override fun onStop() {
        super.onStop()
        historySearch.saveHistory(historyTracks)
    }

    //логика поиска и формирования списков
    private fun search() {
        itunesService.search(enterTextButton.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    Log.d("RESPONSE_CODE", "Status code: ${response.code()}")
                    Log.d("RESPONSE_BODY", "Status code: ${response.body()?.results}")
                    if (response.code() == SUCCESS) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            recyclerTrack.adapter?.notifyDataSetChanged()
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

    //определение статуса от сервера и вывод визуализации представлений
    private fun setStatus(status: SearchStatus) {
        when (status) {
            SearchStatus.CONNECTION_ERROR -> {
                errNoConnect.visibility = View.VISIBLE
                searchHistoryFragment.visibility = View.GONE
                recyclerTrack.visibility = View.GONE
                errNotFound.visibility = View.GONE
            }

            SearchStatus.EMPTY_SEARCH -> {
                errNotFound.visibility = View.VISIBLE
                searchHistoryFragment.visibility = View.GONE
                errNoConnect.visibility = View.GONE
                recyclerTrack.visibility = View.GONE
            }

            SearchStatus.SUCCESS -> {
                recyclerTrack.visibility = View.VISIBLE
                searchHistoryFragment.visibility = View.GONE
                errNoConnect.visibility = View.GONE
                errNotFound.visibility = View.GONE
            }

            SearchStatus.HISTORY -> {
                searchHistoryFragment.visibility = View.VISIBLE
                errNoConnect.visibility = View.GONE
                errNotFound.visibility = View.GONE
                recyclerTrack.visibility = View.GONE
            }

            SearchStatus.ALL_GONE -> {
                searchHistoryFragment.visibility = View.GONE
                errNoConnect.visibility = View.GONE
                errNotFound.visibility = View.GONE
                recyclerTrack.visibility = View.GONE
            }
        }
    }

    //проверка и добавление трека в историю поиска
    private fun addToRecentHistoryList(track: Track) {

        //поиск трека в истории historyTracks ArrayList<Track>
        for (index in historyTracks.indices) {
            if (historyTracks[index].trackId == track.trackId) {
                historyTracks.removeAt(index) //если найден трек в истории по ID удаляем его
                historyTracks.add(0, track) //и добавляем в начало списка
                recyclerHistoryTrack.adapter?.notifyItemMoved(index, 0)
                return
                Log.d("addToRecentHistoryList", "removeAt: ${track.trackId}")
            }
        }
        // задаем размер списка LISTSIZE = 10
        if (historyTracks.size < LISTSIZE) {
            historyTracks.add(0, track)
            recyclerHistoryTrack.adapter?.notifyItemInserted(0)
            recyclerHistoryTrack.adapter?.notifyItemRangeChanged(
                0,
                historyTracks.size
            )
            Log.d("addToRecentHistoryList", "historyTracks.size < 10: ${track.trackId}")
        } else {
            historyTracks.removeAt(9)
            recyclerHistoryTrack.adapter?.notifyItemRemoved(0)
            recyclerHistoryTrack.adapter?.notifyItemRangeChanged(
                9,
                historyTracks.size
            )
            Log.d("addToRecentHistoryList", "removeAt ${track.trackId}")
        }
    }


    //сохранение своих данных
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchText = enterTextButton.text.toString()
        outState.putString(SEARCH_INPUT, searchText)
    }

    //служит для восстановления данных
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedText = savedInstanceState.getString(SEARCH_INPUT)
        enterTextButton.setText(savedText)
    }
}