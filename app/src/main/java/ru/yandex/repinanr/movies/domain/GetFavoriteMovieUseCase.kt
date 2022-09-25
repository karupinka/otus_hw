package ru.yandex.repinanr.movies.domain

import android.content.Context
import ru.yandex.repinanr.movies.data.model.DataModel

class GetFavoriteMovieUseCase(private val repository: MoviesListRepository) {

    suspend fun getFavoriteMovie(id: Int, context: Context): DataModel.Movie? {
        val movie = repository.getFavoriteMovie(id, context)
        movie?.let {
            return DataModel.Movie(
                movieId = it.serviceId,
                name = it.name,
                description = it.description,
                imageUrl = it.imageUrl,
                isFavorite = true
            )
        }
        return null
    }
}