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

    //объекты спсика
    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter()

    //объекты API
    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    private val service = retrofit.create(ApiAppleItunes::class.java)

    //объекты VIEW
    private lateinit var enterInput: EditText
    private lateinit var back: View
    private lateinit var clearButton: ImageView
    private lateinit var rTrack: RecyclerView

    //константы
    companion object {
        const val SEARCH_INPUT = "SEARCH_INPUT"
        const val SUCCESS = 200
    }

    //ответы от сервера API
    enum class SearchStatus {
        CONNECTION_ERROR,
        EMPTY_SEARCH,
        SUCCESS
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //кнопка Назад
        back = findViewById<View>(R.id.search_vector).apply {
            setOnClickListener {
                finish()
            }
        }

        //кнопка очистки текста
        clearButton = findViewById<ImageView>(R.id.clearButtonSearch).apply {
            setOnClickListener {
                enterInput.text = null
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(enterInput.windowToken, 0)
            }
        }

        //поле ввода текста на клавиатуре
        enterInput = findViewById<EditText?>(R.id.editTextSearch).apply { requestFocus() }
        enterInput.doOnTextChanged { text, _, _, _ ->
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
        enterInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

        adapter.trackList = tracks
        rTrack = findViewById(R.id.recyclerView)
        rTrack.adapter = adapter
    }


    //обработка запроса на поиск
    private fun search() {
        service.search(enterInput.text.toString())
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
                            adapter.notifyDataSetChanged()
                            setStatus(SearchStatus.SUCCESS)
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

    //определение статуса от сервера
    private fun setStatus(status: SearchStatus) {
        val nothingFound = findViewById<View>(R.id.err_not_found)
        val networkIssue = findViewById<View>(R.id.err_no_connection)
        when (status) {
            SearchStatus.CONNECTION_ERROR -> {
                networkIssue.visibility = View.VISIBLE
                rTrack.visibility = View.GONE
                nothingFound.visibility = View.GONE
            }
            SearchStatus.EMPTY_SEARCH -> {
                nothingFound.visibility = View.VISIBLE
                networkIssue.visibility = View.GONE
                rTrack.visibility = View.GONE
            }
            SearchStatus.SUCCESS -> {
                rTrack.visibility = View.VISIBLE
                networkIssue.visibility = View.GONE
                nothingFound.visibility = View.GONE
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchText = enterInput.text.toString()
        outState.putString(SEARCH_INPUT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedText = savedInstanceState.getString(SEARCH_INPUT)
        enterInput.setText(savedText)
    }
}