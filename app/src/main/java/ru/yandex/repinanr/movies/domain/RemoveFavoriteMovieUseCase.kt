package ru.yandex.repinanr.movies.domain

import javax.inject.Inject

class RemoveFavoriteMovieUseCase @Inject constructor(
    private val repository: MoviesListRepository
) {

    operator fun invoke(movieId: Int) = repository.removeFavoriteMovie(movieId)
}