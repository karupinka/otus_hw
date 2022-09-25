package ru.yandex.repinanr.movies.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "service_id") val serviceId: Int,
    val name: String,
    val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String
)