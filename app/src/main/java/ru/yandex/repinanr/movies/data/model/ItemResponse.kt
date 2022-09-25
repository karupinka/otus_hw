package ru.yandex.repinanr.movies.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemResponse(
    @SerializedName("total")
    val total: String,
    @SerializedName("totalPages")
    val totalPages: Int,
    @SerializedName("items")
    val items: List<MovieResponse>
) : Serializable
