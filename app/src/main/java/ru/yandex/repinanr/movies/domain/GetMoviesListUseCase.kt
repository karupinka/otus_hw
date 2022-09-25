package ru.yandex.repinanr.movies.domain

import retrofit2.Response
import ru.yandex.repinanr.movies.data.model.ItemResponse

class GetMoviesListUseCase(private val repository: MoviesListRepository) {

    suspend fun getMoviesList(page: Int): Response<ItemResponse> {
        return repository.getMoviesList(page)
    }
}