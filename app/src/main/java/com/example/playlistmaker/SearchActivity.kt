package com.example.playlistmaker

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
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Network.API.ApiAppleItunes
import com.example.playlistmaker.Network.API.TrackResponse
import com.example.playlistmaker.RecyclerV.Track
import com.example.playlistmaker.RecyclerV.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//работа с интерфейсом ПОИСК
class SearchActivity : AppCompatActivity() {

    //константы
    companion object {
        const val SEARCH_INPUT = "SEARCH_INPUT"
        const val COD200 = 200
    }

    //объекты спсика
    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter()

    //объекты API
    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    private val service = retrofit.create(ApiAppleItunes::class.java)

    //объекты VIEW
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var enterTextButton: EditText
    private lateinit var clearButton: ImageView
    private lateinit var backButton: View


    //ответы от сервера API
    enum class SearchStatus {
        CONNECTION_ERROR,
        EMPTY_SEARCH,
        COD200
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //кнопка Назад
        backButton = findViewById<View>(R.id.search_vector).apply {
            setOnClickListener {
                finish()
            }
        }

        //кнопка очистки текста
        clearButton = findViewById<ImageView>(R.id.clearButtonSearch).apply {
            setOnClickListener {
                enterTextButton.text = null
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(enterTextButton.windowToken, 0)
            }


        }

        //поле ввода текста на клавиатуре
        enterTextButton = findViewById<EditText?>(R.id.editTextSearch).apply { requestFocus() }
        enterTextButton.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                clearButton.visibility = View.INVISIBLE
            } else {
                clearButton.visibility = View.VISIBLE
            }
        }

        //обновление запроса
        val refresh = findViewById<Button>(R.id.refreshButton).apply {
            setOnClickListener { search() }
        }

        //поиск по запросу
        enterTextButton.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

        trackAdapter.trackList = tracks
        recyclerTrack = findViewById(R.id.recyclerView)
        recyclerTrack.adapter = trackAdapter
    }

    //обработка запроса на поиск
    private fun search() {
        service.search(enterTextButton.text.toString())
            .enqueue(object : Callback<TrackResponse> {

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {

                    if (response.code() == COD200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                            setStatus(SearchStatus.COD200)
                        }
                        if (tracks.isEmpty()) {
                            setStatus(SearchStatus.EMPTY_SEARCH)
                        }
                    }
                }

                //В случае попадания запроса в метод onFailure() или отличии кода статуса запроса от 200, показывать эту заглушку
                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    setStatus(SearchStatus.CONNECTION_ERROR)
                }

            })
    }

    //определение статуса от сервера и вывод визуализации представлений
    private fun setStatus(status: SearchStatus) {
        val errNotFound = findViewById<View>(R.id.err_not_found)
        val errNoConnect = findViewById<View>(R.id.err_no_connection)
        when (status) {
            SearchStatus.CONNECTION_ERROR -> {
                errNoConnect.visibility = View.VISIBLE
                recyclerTrack.visibility = View.GONE
                errNotFound.visibility = View.GONE
            }
            SearchStatus.EMPTY_SEARCH -> {
                errNotFound.visibility = View.VISIBLE
                errNoConnect.visibility = View.GONE
                recyclerTrack.visibility = View.GONE
            }
            SearchStatus.COD200 -> {
                recyclerTrack.visibility = View.VISIBLE
                errNoConnect.visibility = View.GONE
                errNotFound.visibility = View.GONE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchText = enterTextButton.text.toString()
        outState.putString(SEARCH_INPUT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedText = savedInstanceState.getString(SEARCH_INPUT)
        enterTextButton.setText(savedText)
    }
}