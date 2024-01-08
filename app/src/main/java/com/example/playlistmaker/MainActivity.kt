package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.Network.Untold
import com.example.playlistmaker.Network.Untold.Companion.checkInternetReachability

class MainActivity : AppCompatActivity() {
    //@SuppressLint("MissingInflatedId")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingButton = findViewById<Button>(R.id.setting_button)

        //проверка состояния сети   ...
        println(Untold())

        searchButton.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        mediaButton.setOnClickListener {
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }

        settingButton.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }


        /*
        //setting_button
        val search_button = findViewById<Button>(R.id.search_button)
        search_button.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на ПОИСК!", Toast.LENGTH_SHORT).show()
        }

        //setting_button
        val media_button = findViewById<Button>(R.id.media_button)
        media_button.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на МЕДИА!", Toast.LENGTH_SHORT).show()
        }


        //setting_button
        val setting_button = findViewById<Button>(R.id.setting_button)
        setting_button.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на НАСТРОЙКИ!", Toast.LENGTH_SHORT).show()
        }
        */

    }
}