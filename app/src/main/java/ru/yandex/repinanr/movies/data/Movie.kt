package ru.yandex.repinanr.movies.data

import java.io.Serializable

data class Movie(
    val movieId: Int,
    val name: String,
    var description: String,
    val image: Int,
    var isFavorite: Boolean? = null,
    var comment: String = ""
): Serializable
