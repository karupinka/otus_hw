package ru.yandex.repinanr.movies.domain

import javax.inject.Inject

class GetFavoriteMovieListUseCase @Inject constructor(
    private val repository: MoviesListRepository
) {

    operator fun invoke() = repository.getFavoriteMoviesList()
}