package ru.yandex.repinanr.movies.data

import androidx.annotation.DrawableRes
import java.io.Serializable

sealed class DataModel: Serializable {
    data class Movie(
        val movieId: Int,
        val name: String,
        var description: String,
        @DrawableRes val image: Int,
        val isFavorite: Boolean = false,
        val comment: String = ""
    ): Serializable, DataModel()
}
