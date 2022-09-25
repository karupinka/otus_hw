package ru.yandex.repinanr.movies.data.model

import androidx.annotation.DrawableRes
import java.io.Serializable

sealed class DataModel: Serializable {
    data class Movie(
        val movieId: Int,
        val name: String,
        val description: String = "",
        @DrawableRes val image: Int = 0,
        val imageUrl: String? = null,
        val isFavorite: Boolean = false,
        val comment: String = ""
    ): Serializable, DataModel()
}
