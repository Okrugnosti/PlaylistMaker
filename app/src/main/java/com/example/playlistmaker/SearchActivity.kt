package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
    }

    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchVector = findViewById<ImageView>(R.id.search_vector)
        val editText = findViewById<EditText>(R.id.editTextSearch)
        val clearButtonSearch = findViewById<ImageView>(R.id.clearButtonSearch)


        //КНОПКА ВОЗВРАТА В ГЛАВНОЕ МЕНЮ
        searchVector.setOnClickListener {

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

        //ОЧИСТКА ТЕКСТА В ПОЛЕ ВВОДА
        clearButtonSearch.setOnClickListener {
            editText.setText("")
        }


        val simpleTextWatcher = object : TextWatcher {

            //заменя символов новым теĸстом
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            //событие используется, ĸогда нужно увидеть, ĸаĸие символы в теĸсте являются новыми
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                /*
                if (s.isNullOrEmpty()) {
                    linearLayout.setBackgroundColor(getColor(R.color.prime_neutral))
                } else {
                    val input = s.toString()
                    if (isPrime(input.toInt())) {
                        linearLayout.setBackgroundColor(getColor(R.color.prime_positive))
                    } else {
                        linearLayout.setBackgroundColor(getColor(R.color.prime_negative))
                    }
                }
                */

                //включает функцию визуализации кнопки очистки
                clearButtonSearch.visibility = clearButtonSearchVisibility(s)
            }

            //событие используется, когда нужно увидеть и, возможно, отредаĸтировать новый теĸст
            override fun afterTextChanged(s: Editable?) {
                // empty
            }

        }

        //устанавливает для объекта EditText созданый TextWatcher
        editText.addTextChangedListener(simpleTextWatcher)
        this.editText = findViewById<EditText?>(R.id.editTextSearch)
    }


    // ЛОГИКА ВИЗУАЛИЗАЦИИ КНОПКИ ОЧИСТКИ ТЕКСТА
    private fun clearButtonSearchVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchText = editText.text.toString()
        outState.putString(PRODUCT_AMOUNT, searchText)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedText = savedInstanceState.getString(PRODUCT_AMOUNT)
        editText.setText(savedText)

    }

}