package ru.yandex.repinanr.movies.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import retrofit2.Response
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.model.ItemResponse
import ru.yandex.repinanr.movies.data.model.MovieResponse
import ru.yandex.repinanr.movies.data.room.CommentsEntity
import ru.yandex.repinanr.movies.data.room.FavoriteMovieEntity

interface MoviesListRepository {

    suspend fun getMovieItem(id: Int): Response<MovieResponse>

    suspend fun getMoviesList(page: Int): Response<ItemResponse>

    suspend fun getFavoriteMoviesList(context: Context): List<FavoriteMovieEntity>?

    suspend fun getFavoriteMovie(movieId: Int, context: Context): FavoriteMovieEntity?

    suspend fun removeFavoriteMovie(movie: FavoriteMovieEntity, context: Context)

    suspend fun addFavoriteMovie(movie: FavoriteMovieEntity, context: Context)

    suspend fun getMovieComment(movieId: Int, context: Context): CommentsEntity?

    suspend fun setMovieComment(commentsEntity: CommentsEntity, context: Context)

    fun letMoviesList(context: Context): LiveData<PagingData<DataModel.Movie>>
}