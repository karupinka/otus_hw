package ru.yandex.repinanr.movies.domain

import ru.yandex.repinanr.movies.data.model.DataModel.Movie
import javax.inject.Inject

class AddFavoriteMovieUseCase @Inject constructor(
    private val repository: MoviesListRepository
) {

    operator fun invoke(movie: Movie) = repository.addFavoriteMovie(movie)
}