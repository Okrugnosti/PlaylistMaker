package com.example.playlistmaker.main


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.SettingActivity
import com.example.playlistmaker.library.LibraryActivity
import com.example.playlistmaker.search.SearchActivity

//реализация главного экрана

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //кнопка ПОИСК
        val searchButton = findViewById<Button>(R.id.search)
        searchButton.setOnClickListener {
            val displaySearchIntent = Intent(this, SearchActivity::class.java)
            startActivity(displaySearchIntent)
        }

        //кнопка МЕДИАТЕКА
        val libraryButton = findViewById<Button>(R.id.library)
        libraryButton.setOnClickListener {
            val displayLibraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(displayLibraryIntent)
        }

        //кнопка НАСТРОЙКИ
        val settingsButton = findViewById<Button>(R.id.settings)
        settingsButton.setOnClickListener {
            val displaySettingsIntent = Intent(this, SettingActivity::class.java)
            startActivity(displaySettingsIntent)
        }
    }
}