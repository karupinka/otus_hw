package ru.yandex.repinanr.movies.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface MoviesDao {

    @Insert(onConflict = REPLACE)
    fun insert(movie: MovieEntity)

    @Insert(onConflict = REPLACE)
    fun insertAll(movie: List<MovieEntity>)

    @Update(onConflict = REPLACE)
    fun update(movie: MovieEntity?)

    @Query("SELECT * FROM MOVIES")
    fun getAllMovies(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM MOVIES WHERE service_id=:id")
    suspend fun getMovie(id: Int): MovieEntity?

    @Query("DELETE FROM MOVIES")
    suspend fun removeMovies()
}