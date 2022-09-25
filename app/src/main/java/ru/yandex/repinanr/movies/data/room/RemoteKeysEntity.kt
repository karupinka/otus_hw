package ru.yandex.repinanr.movies.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey
    @ColumnInfo(name = "repo_id") val repoId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)