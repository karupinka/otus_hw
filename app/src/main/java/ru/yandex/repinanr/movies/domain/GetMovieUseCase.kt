package ru.yandex.repinanr.movies.domain

import retrofit2.Response
import ru.yandex.repinanr.movies.data.model.MovieResponse

class GetMovieUseCase(private val repository: MoviesListRepository) {

    suspend fun getMovie(id: Int): Response<MovieResponse> {
        return repository.getMovieItem(id)
    }
}