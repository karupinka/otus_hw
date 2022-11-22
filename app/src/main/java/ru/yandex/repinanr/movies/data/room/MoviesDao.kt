package ru.yandex.repinanr.movies.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface MoviesDao {

    @Insert(onConflict = REPLACE)
    fun insertAll(movie: List<MovieEntity>)

    @Query("SELECT * FROM MOVIES")
    fun getAllMovies(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM MOVIES WHERE service_id=:id")
    fun getMovie(id: Int): Observable<MovieEntity>?

    @Query("DELETE FROM MOVIES")
    fun removeMovies()
}