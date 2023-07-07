package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val settingButton = findViewById<ImageView>(R.id.setting_button_back2)
        val sendLinks = findViewById<ImageView>(R.id.sendLinks)
        val mailSupport = findViewById<ImageView>(R.id.mailSupport)
        val links = findViewById<ImageView>(R.id.links)


        //ПОДЕЛИТЬСЯ ПРИЛОЖЕНИЕМ
        sendLinks.setOnClickListener {
            val data = "https://practicum.yandex.ru/android-developer/"
            val openLinks = Intent(Intent.ACTION_SEND)
            openLinks.putExtra(Intent.EXTRA_TEXT, data)
            openLinks.setType("text/plain")
            startActivity(openLinks)
        }


        //НАЗАД В ГЛАВНОЕ МЕНЮ
        settingButton.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }


        //ОТПРАВКА ПИСЬМА В ТЕХ.ПОДДЕРЖКУ
        mailSupport.setOnClickListener {
            //val mailAddress = Uri.parse("mail: mail@mail.ru")
            val item = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val textMail = "Спасибо разработчикам и разработчицам за крутое приложение!"

            val sendMail = Intent(Intent.ACTION_SENDTO)
            //sendMail.putExtra(Intent.EXTRA_EMAIL, mailAddress)
            sendMail.putExtra(Intent.EXTRA_SUBJECT, item)
            sendMail.putExtra(Intent.EXTRA_TEXT, textMail)
            sendMail.setData(Uri.parse("mailto:"))
            startActivity(sendMail)
        }


        //ПЕРЕХОД НА ПОЛЬЗОВАТЕЛЬСКОЕ СОГЛАШЕНИЕ
        links.setOnClickListener {
            val address = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val openLinks = Intent(Intent.ACTION_VIEW, address)
            startActivity(openLinks)
        }

    }
}