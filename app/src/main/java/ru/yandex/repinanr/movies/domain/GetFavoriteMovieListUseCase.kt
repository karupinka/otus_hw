package ru.yandex.repinanr.movies.domain

import android.content.Context
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.room.Db
import ru.yandex.repinanr.movies.data.room.FavoriteMovieEntity

class GetFavoriteMovieListUseCase(private val repository: MoviesListRepository) {

    suspend fun getFavoriteMovieList(context: Context): List<FavoriteMovieEntity>? {
        val response = Db.getInstance(context)?.getFavoriteMovieDao()?.getAllMovies()
        val currentList = mutableListOf<DataModel.Movie>()
        response?.let { list ->
            list.forEach {
                    val movieTmp = DataModel.Movie(
                        movieId = it.serviceId,
                        name = it.name,
                        description = it.description,
                        imageUrl = it.imageUrl,
                        isFavorite = true
                    )
                    currentList.add(movieTmp)
            }

            return list
        }
        return null
    }
}