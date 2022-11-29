package ru.yandex.repinanr.movies.domain

import javax.inject.Inject

class InsertCommentMovieUseCase @Inject constructor(
    private val repository: MoviesListRepository
) {

    operator fun invoke(id: Int, comment: String) = repository.setMovieComment(id, comment)
}