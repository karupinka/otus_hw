package ru.yandex.repinanr.movies.domain

import javax.inject.Inject

class LetMovieUseCase @Inject constructor(
    private val repository: MoviesListRepository
) {

    operator fun invoke() = repository.letMoviesList()
}