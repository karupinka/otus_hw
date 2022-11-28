package ru.yandex.repinanr.movies.domain

import javax.inject.Inject

class GetMovieUseCase @Inject constructor(
    private val repository: MoviesListRepository
) {

    operator fun invoke(id: Int) = repository.getMovieItem(id)
}