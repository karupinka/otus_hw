package ru.yandex.repinanr.movies.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface FavoriteMoviesDao {

    @Insert(onConflict = REPLACE)
    fun insert(movie: FavoriteMovieEntity): Completable

    @Query("DELETE FROM favorite_movies WHERE service_id=:id")
    fun delete(id: Int): Completable

    @Query("SELECT * FROM favorite_movies")
    fun getAllMovies(): Observable<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM favorite_movies")
    fun getAllMoviesSingle(): Single<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM favorite_movies WHERE service_id=:id")
    fun getMovie(id: Int): Flowable<List<FavoriteMovieEntity>>
}