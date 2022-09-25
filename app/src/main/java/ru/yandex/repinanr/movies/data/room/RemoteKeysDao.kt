package ru.yandex.repinanr.movies.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remote_keys WHERE repo_id = :id")
    suspend fun remoteKeysId(id: Int): RemoteKeysEntity

    @Query("DELETE FROM remote_keys")
    suspend fun removeRemoteKeys()
}