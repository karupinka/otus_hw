package ru.yandex.repinanr.movies.domain

import androidx.paging.PagingData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import ru.yandex.repinanr.movies.data.model.DataModel.Movie
import ru.yandex.repinanr.movies.data.model.ItemResponse
import ru.yandex.repinanr.movies.data.room.FavoriteMovieEntity

interface MoviesListRepository {

    fun getMovieItem(id: Int, isFavorite: Boolean): Flowable<Movie>

    fun getMoviesList(page: Int): Single<ItemResponse>

    fun getFavoriteMoviesList(): Observable<List<Movie>>

    fun getIsFavoriteMovie(movieId: Int): Flowable<Boolean>

    fun removeFavoriteMovie(movieId: Int): Completable

    fun addFavoriteMovie(movie: Movie): Completable

    fun getMovieComment(movieId: Int): Flowable<String>

    fun setMovieComment(id: Int, comment: String): Completable

    fun letMoviesList(): Flowable<PagingData<Movie>>

    fun getAllFavoriteMoviesSingle(): Single<List<FavoriteMovieEntity>>
}