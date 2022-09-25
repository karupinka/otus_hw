package ru.yandex.repinanr.movies.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies",
    indices = [
        Index(value = ["service_id"]),
        Index(value = ["name"])
    ]
)
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "service_id") val serviceId: Int,
    val name: String,
    val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String
)