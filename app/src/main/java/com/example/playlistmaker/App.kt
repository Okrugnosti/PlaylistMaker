package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

/*
Вы уже изучали понятие контекста в Android и знаете, что тема может быть применена как к текущей Activity, так и ко всему приложению. В нашем случае экран настроек должен распространять изменение темы на всё приложение, а не только на текущий экран, поэтому требуются небольшие доработки.
Создайте класс App и унаследуйте от класса Application, а затем реализуйте в нём метод onCreate
*/

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        /*
        Введите индикатор darkTheme внутрь класса App со значением false по умолчанию,
        а в метод onCreate установите ему значение из SharedPreferences.
         */

                AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}