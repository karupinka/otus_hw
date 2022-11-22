package ru.yandex.repinanr.movies.data.model

import java.io.Serializable

sealed class DataModel: Serializable {
    data class Movie(
        val movieId: Int,
        val name: String,
        val description: String = "",
        val imageUrl: String? = null,
        val isFavorite: Boolean = false,
        val comment: String = ""
    ): Serializable, DataModel()
}
