package com.example.playlistmaker.RecyclerV

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    // В этом методе создаётся (или «надувается» от англ. inflate) элемент ViewHolder.
    // В процессе работы onCreateViewHolder мы получаем из xml-разметки элемента готовый
    // к работе объект ViewHolder с инициализированными переменными UI-компонент.
    // Каждый вызов метода создаёт одну отображаемую View внутри ViewHolder
    var trackList = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    //Возвращает количество элементов списка.
    override fun getItemCount() = trackList.size

    // тут происходит установка значений UI-компонент из модели данных.
    // Метод onBindViewHolder отвечает за корректное переиспользование существующей View.
    // Нам нужно передать данные об объекте определённой позиции и сказать ViewHolder,
    // как отобразить конкретный элемент — описать правила, как должна выглядеть эта View
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

}