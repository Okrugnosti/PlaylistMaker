package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.Creator
import com.example.playlistmaker.player.domain.model.Track
import com.example.playlistmaker.player.presentation.PlayerPresenterImplemented
import com.example.playlistmaker.player.presentation.PlayerView
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity(), PlayerView {

    private lateinit var presenter : PlayerPresenterImplemented
    private val paBackButton: ImageView by lazy { findViewById(R.id.back_arrow) }
    private val paSongCover: ImageView by lazy { findViewById(R.id.cover) }
    private val paSongName: TextView by lazy { findViewById(R.id.song_name) }
    private val paSongArtist: TextView by lazy { findViewById(R.id.song_artist) }
    private val paSongDuration: TextView by lazy { findViewById(R.id.duration) }
    private val paSongAlbum: TextView by lazy { findViewById(R.id.album_name) }
    private val paAlbumTitle: TextView by lazy { findViewById(R.id.album_title) }
    private val paSongYear: TextView by lazy { findViewById(R.id.year) }
    private val paSongGenre: TextView by lazy { findViewById(R.id.genre) }
    private val paSongCountry: TextView by lazy { findViewById(R.id.country) }
    private val paButtonPlay: ImageView by lazy { findViewById(R.id.play_button) }
    private val paSecondsPassed: TextView by lazy { findViewById(R.id.time_played) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        paSongAlbum.visibility = View.VISIBLE
        paAlbumTitle.visibility = View.VISIBLE

        presenter = Creator.connectPresenter(playerView = this, activity = this)

        setupListeners()
    }

    private fun setupListeners(){
        paBackButton.setOnClickListener { presenter.onIvBackPressed() }
        paButtonPlay.setOnClickListener {
            presenter.onButtonClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.onViewPaus()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroy()
    }

    override fun getData(track: Track) {
        Glide.with(this).load(track.artworkUrl100.replaceAfterLast(DELIMITER, PROPER_DIMENSIONS))
            .placeholder(R.drawable.placeholder).centerInside()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.margin_8dp)))
            .into(paSongCover)

        paSongDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        paSongAlbum.text = track.collectionName.ifEmpty {
            ({
                paSongAlbum.visibility = View.GONE
                paAlbumTitle.visibility = View.GONE
            }).toString()
        }

        paSongName.text = track.trackName
        paSongArtist.text = track.artistName
        paSongYear.text = track.releaseDate.subSequence(ZERO, FOUR)
        paSongGenre.text = track.primaryGenreName
        paSongCountry.text = track.country
        paSecondsPassed.text = "00:00"
    }

    override fun goBack() {
        finish()
    }

    override fun setPauseImage() {
        paButtonPlay.setImageResource(R.drawable.pause_button)
    }

    override fun setStartImage() {
        paButtonPlay.setImageResource(R.drawable.play_button)
    }

    override fun updateTimePlayed(timePlayed: String) {
        paSecondsPassed.text = timePlayed
    }

    companion object {
        private const val FOUR = 4
        private const val ZERO = 0
        private const val DELIMITER = '/'
        private const val PROPER_DIMENSIONS = "512x512bb.jpg"
    }
}