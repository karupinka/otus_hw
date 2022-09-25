package ru.yandex.repinanr.movies.domain

import android.content.Context

class GetCommentMovieUseCase(private val repository: MoviesListRepository) {

    suspend fun getMovieComment(id: Int, context: Context): String {
        return repository.getMovieComment(id, context)?.comment ?: ""
    }
}