package com.example.playlistmaker.settings

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.playlistmaker.R
import com.example.playlistmaker.app.App
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back = findViewById<View>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        val themeSwitch = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitch.isChecked = (applicationContext as App).darkTheme
        themeSwitch.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        val shareApp = findViewById<ImageView>(R.id.share_app).apply {
            setOnClickListener {
                val shareIntentLink = Intent(Intent.ACTION_SEND)
                shareIntentLink.type = "text/html"
                shareIntentLink.putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_link))
                startActivity(Intent.createChooser(shareIntentLink, getString(R.string.select)))
            }
        }

        val supportMail = findViewById<ImageView>(R.id.support).apply {
            setOnClickListener {
                val emailSubject = getString(R.string.email_subject)
                val message = getString(R.string.email_body)
                val shareIntentMail = Intent(Intent.ACTION_SENDTO)
                shareIntentMail.data = Uri.parse("mailto:")
                shareIntentMail.putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(getString(R.string.developer_email))
                )
                shareIntentMail.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                shareIntentMail.putExtra(Intent.EXTRA_TEXT, message)
                startActivity(
                    Intent.createChooser(
                        shareIntentMail,
                        getString(R.string.select_mail)
                    )
                )
            }
        }

        val userDogovor = findViewById<ImageView>(R.id.user_agreement).apply {
            setOnClickListener {
                val url = Uri.parse(getString(R.string.user_agreement_link))
                startActivity(Intent(Intent.ACTION_VIEW, url))
            }
        }
    }
}