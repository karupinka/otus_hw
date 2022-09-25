package ru.yandex.repinanr.movies.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments"
)
data class CommentsEntity(
    @PrimaryKey
    @ColumnInfo(name = "service_id") val serviceId: Int,
    val comment: String
)