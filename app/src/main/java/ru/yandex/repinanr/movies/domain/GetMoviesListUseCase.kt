package ru.yandex.repinanr.movies.domain

import io.reactivex.Single
import ru.yandex.repinanr.movies.data.model.ItemResponse
import javax.inject.Inject

class GetMoviesListUseCase @Inject constructor(
    private val repository: MoviesListRepository
) {

    operator fun invoke(page: Int): Single<ItemResponse> = repository.getMoviesList(page)
}