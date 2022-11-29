package ru.yandex.repinanr.movies.domain

import javax.inject.Inject

class GetCommentMovieUseCase @Inject constructor(
    private val repository: MoviesListRepository
) {

    operator fun invoke(id: Int) = repository.getMovieComment(id)
}