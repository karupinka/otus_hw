package ru.yandex.repinanr.movies.domain

import android.content.Context
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.room.Db
import ru.yandex.repinanr.movies.data.room.FavoriteMovieEntity

class AddFavoriteMovieUseCase(private val repository: MoviesListRepository) {

    suspend fun addFavoriteMovie(movie: DataModel.Movie, context: Context) {
        Db.getInstance(context)?.getFavoriteMovieDao()?.insert(
            FavoriteMovieEntity(
                movie.movieId,
                movie.name,
                "",
                movie.imageUrl ?: ""
            )
        )
    }
}