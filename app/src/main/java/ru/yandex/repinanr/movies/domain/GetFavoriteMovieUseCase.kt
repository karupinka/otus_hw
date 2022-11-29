package ru.yandex.repinanr.movies.domain

import javax.inject.Inject

class GetFavoriteMovieUseCase @Inject constructor(
    private val repository: MoviesListRepository
) {

    operator fun invoke(id: Int) = repository.getIsFavoriteMovie(id)
}