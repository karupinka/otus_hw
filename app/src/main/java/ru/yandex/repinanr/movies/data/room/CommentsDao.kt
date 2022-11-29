package ru.yandex.repinanr.movies.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface CommentsDao {

    @Insert(onConflict = REPLACE)
    fun insert(movie: CommentsEntity): Completable

    @Query("SELECT * FROM comments WHERE service_id=:id")
    fun getComment(id: Int): Flowable<List<CommentsEntity>>
}