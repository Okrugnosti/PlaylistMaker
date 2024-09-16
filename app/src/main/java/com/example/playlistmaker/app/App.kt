package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

/*
Контекст - реализация приминения темной темы ко всему приложению
Класс App наследуйт от класса Application
с Реализацией нём метод onCreate.
Индикатор darkTheme внутри класса App со значением false по умолчанию,
В методе onCreate устанавливается значение из SharedPreferences.
*/

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}