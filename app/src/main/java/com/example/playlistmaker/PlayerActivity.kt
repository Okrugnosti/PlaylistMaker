package com.example.playlistmaker

import android.app.usage.NetworkStats.Bucket.STATE_DEFAULT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.RecyclerV.Track
import com.example.playlistmaker.Search.SearchActivity.Companion.SEARCH_INPUT
import java.text.SimpleDateFormat
import java.util.Locale
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

private const val FOUR = 4
private const val ZERO = 0
private const val DELIMITER = '/'
private const val PROPER_DIMENSIONS = "512x512bb.jpg"

class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private val ivBackButton: ImageView by lazy { findViewById(R.id.back_arrow) }
    private val ivSongCover: ImageView by lazy { findViewById(R.id.image_track) }
    private val tvSongName: TextView by lazy { findViewById(R.id.song_name) }
    private val tvSongArtist: TextView by lazy { findViewById(R.id.song_author) }
    private val tvSongDuration: TextView by lazy { findViewById(R.id.duration) }
    private val tvSongAlbum: TextView by lazy { findViewById(R.id.album_name) }
    private val tvAlbumTitle: TextView by lazy { findViewById(R.id.title_album) }
    private val tvSongYear: TextView by lazy { findViewById(R.id.track_year) }
    private val tvSongGenre: TextView by lazy { findViewById(R.id.track_genre) }
    private val tvSongCountry: TextView by lazy { findViewById(R.id.track_artist_country) }
    private val tvTimeTrack: TextView by lazy { findViewById(R.id.time_track) }
    private val btPlay: ImageView by lazy { findViewById(R.id.button_play) }
    private val tvSecondsPassed: TextView by lazy { findViewById(R.id.time_track) }

    private var mainThreadHandler: Handler? = null
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var url: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        tvSongAlbum.visibility = View.VISIBLE
        tvAlbumTitle.visibility = View.VISIBLE
        ivBackButton.setOnClickListener { finish() }

        tvSecondsPassed.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)

        mainThreadHandler = Handler(Looper.getMainLooper())

        getData()
        preparePlayer()

        btPlay.setOnClickListener {
            playbackControl()
        }
    }


    private fun getData() {

        track = intent.getParcelableExtra<Track>(SEARCH_INPUT) ?: return

        /*
        Glide.with(this)
            .load(track.artworkUrl100?.replaceAfterLast(DELIMITER, PROPER_DIMENSIONS))
            .placeholder(R.drawable.placeholder)
            .centerInside()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.mar_8dp)))
            .into(ivSongCover)
        */

        Glide.with(this).load(track.artworkUrl100?.replaceAfterLast(DELIMITER, PROPER_DIMENSIONS))
            .placeholder(R.drawable.placeholder).centerInside()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.mar_8dp)))
            .into(ivSongCover)


        tvSongDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)

        //отображение времени проигрывания, до реализации логики проигрывания трека
        tvTimeTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)

        tvSongAlbum.text = if (track.collectionName.isNullOrEmpty()) ({
            tvSongAlbum.visibility = View.GONE
            tvAlbumTitle.visibility = View.GONE
        }).toString() else track.collectionName

        tvSongName.text = track.trackName
        tvSongArtist.text = track.artistName
        tvSongYear.text = track.releaseDate.subSequence(ZERO, FOUR)
        tvSongGenre.text = track.primaryGenreName
        tvSongCountry.text = track.country
        url = track.previewUrl

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler?.removeCallbacksAndMessages(null)
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }


    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            btPlay.isEnabled = true
            btPlay.setImageResource(R.drawable.play_day)
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            btPlay.setImageResource(R.drawable.play_day)
            setDuration(0)
            playerState = STATE_PREPARED
            mainThreadHandler?.removeCallbacksAndMessages(null)
        }
    }

    //Плеер играть
    private fun startPlayer() {
        mediaPlayer.start()
        btPlay.setImageResource(R.drawable.pause_day)
        playerState = STATE_PLAYING
        mainThreadHandler?.postDelayed(object : Runnable {
            override fun run() {
                setDuration(mediaPlayer.currentPosition)
                mainThreadHandler?.postDelayed(this, DELAY)
            }
        }, DELAY)
    }

    //Плеейр на паузу
    private fun pausePlayer() {
        mediaPlayer.pause()
        btPlay.setImageResource(R.drawable.play_day)
        playerState = STATE_PAUSED
    }

    //установка длитильности
    private fun setDuration(milliseconds: Int) {
        tvSecondsPassed.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(milliseconds)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }

}