package ru.yandex.repinanr.movies.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface CommentsDao {

    @Insert(onConflict = REPLACE)
    fun insert(movie: CommentsEntity)

    @Query("SELECT * FROM comments WHERE service_id=:id")
    fun getMovie(id: Int): CommentsEntity?
}