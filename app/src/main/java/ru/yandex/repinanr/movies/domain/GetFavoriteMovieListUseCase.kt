package ru.yandex.repinanr.movies.domain

import javax.inject.Inject

open class GetFavoriteMovieListUseCase @Inject constructor(
    private val repository: MoviesListRepository
) {

    open operator fun invoke() = repository.getFavoriteMoviesList()
}