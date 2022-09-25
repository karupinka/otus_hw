package ru.yandex.repinanr.movies.domain

import android.content.Context
import ru.yandex.repinanr.movies.data.room.CommentsEntity
import ru.yandex.repinanr.movies.data.room.Db

class UpdateCommentMovieUseCase(private val repository: MoviesListRepository) {

    suspend fun updateMovieComment(id: Int, comment: String, context: Context) {
        Db.getInstance(context)?.getCommentsDao()?.insert(
            CommentsEntity(id, comment)
        )
    }
}