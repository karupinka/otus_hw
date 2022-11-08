package ru.yandex.repinanr.movies.domain

import android.content.Context
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.room.Db
import ru.yandex.repinanr.movies.data.room.FavoriteMovieEntity

class RemoveFavoriteMovieUseCase(private val repository: MoviesListRepository) {

    suspend fun removeFavoriteMovie(movie: DataModel.Movie, context: Context) {
        Db.getInstance(context)?.getFavoriteMovieDao()?.delete(
            FavoriteMovieEntity(
                movie.movieId,
                movie.name,
                movie.description,
                movie.imageUrl ?: ""
            )
        )
    }

    suspend fun removeFavoriteMovie(movieId: Int, context: Context) {
        Db.getInstance(context)?.getFavoriteMovieDao()?.delete(movieId)
    }
}