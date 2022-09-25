package ru.yandex.repinanr.movies.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MovieResponse(
    @SerializedName("kinopoiskId")
    val id: Int,
    @SerializedName("nameRu")
    val name: String?,
    @SerializedName("nameOriginal")
    val nameOriginal: String?,
    @SerializedName("nameEn")
    val nameEn: String?,
    @SerializedName("posterUrlPreview")
    val previewUrl: String,
    @SerializedName("description")
    val description: String?
) : Serializable
