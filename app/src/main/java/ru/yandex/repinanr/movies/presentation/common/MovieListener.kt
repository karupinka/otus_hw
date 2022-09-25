package ru.yandex.repinanr.movies.presentation.common

import ru.yandex.repinanr.movies.data.model.DataModel

interface MovieListener {
    fun onFavoriteClickListener(movie: DataModel.Movie, position: Int)
    fun onItemClickListener(movie: DataModel.Movie) {
        // DO NOTHING
    }

    fun onRemoveClickListener(movie: DataModel.Movie, position: Int) {
        // DO NOTHING
    }
}