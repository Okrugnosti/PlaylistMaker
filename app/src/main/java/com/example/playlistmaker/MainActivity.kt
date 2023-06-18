package com.example.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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



    }
}