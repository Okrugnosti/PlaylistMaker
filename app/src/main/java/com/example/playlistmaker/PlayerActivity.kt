package com.example.playlistmaker

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

const val FOUR = 4
const val ZERO = 0
const val DELIMITER = '/'
const val PROPER_DIMENSIONS = "512x512bb.jpg"
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        tvSongAlbum.visibility = View.VISIBLE
        tvAlbumTitle.visibility = View.VISIBLE
        ivBackButton.setOnClickListener { finish() }

        track = intent.getParcelableExtra<Track>(SEARCH_INPUT) ?: return

        Glide.with(this)
            .load(track.artworkUrl100?.replaceAfterLast(DELIMITER, PROPER_DIMENSIONS))
            .placeholder(R.drawable.placeholder)
            .centerInside()
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
    }
}