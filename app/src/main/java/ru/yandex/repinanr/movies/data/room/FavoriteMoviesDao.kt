package ru.yandex.repinanr.movies.data.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface FavoriteMoviesDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(movie: FavoriteMovieEntity)

    @Update(onConflict = REPLACE)
    fun update(movie: FavoriteMovieEntity)

    @Delete
    suspend fun delete(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE service_id=:id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM favorite_movies")
    suspend fun getAllMovies(): List<FavoriteMovieEntity>

    @Query("SELECT * FROM favorite_movies WHERE service_id=:id")
    suspend fun getMovie(id: Int): FavoriteMovieEntity?
}