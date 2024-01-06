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


        //НАЗАД В ГЛАВНОЕ МЕНЮ
        settingButton.setOnClickListener {
            /*
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
            */

            /*
            Для перехода назад стоит использовать не интент, а методы finish() или onBackPressed(),
            потому что иначе вместо возврата в предыдущий экран создается новый экземпляр MainActivity
             */
            this.finish()
        }

        //ПОДЕЛИТЬСЯ ПРИЛОЖЕНИЕМ
        sendLinks.setOnClickListener {
            val data = getString(R.string.g4)
            val openLinks = Intent(Intent.ACTION_SEND)
            openLinks.putExtra(Intent.EXTRA_TEXT, data)
            openLinks.setType("text/plain")
            startActivity(openLinks)
        }

        //ОТПРАВКА ПИСЬМА В ТЕХ.ПОДДЕРЖКУ
        mailSupport.setOnClickListener {
            val sendMail = Intent(Intent.ACTION_SENDTO)
            sendMail.setData(Uri.parse("mailto:"))

            sendMail.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.g5))
            )
            sendMail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.g1))
            sendMail.putExtra(Intent.EXTRA_TEXT, getString(R.string.g2))

            startActivity(sendMail)
        }

        //ПЕРЕХОД НА ПОЛЬЗОВАТЕЛЬСКОЕ СОГЛАШЕНИЕ
        links.setOnClickListener {
            val address = Uri.parse(getString(R.string.g3))
            val openLinks = Intent(Intent.ACTION_VIEW, address)
            startActivity(openLinks)
        }

    }
}