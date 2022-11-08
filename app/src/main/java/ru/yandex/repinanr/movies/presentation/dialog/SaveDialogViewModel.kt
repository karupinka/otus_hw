package ru.yandex.repinanr.movies.presentation.dialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.repository.MoviesListRepositoryImpl
import ru.yandex.repinanr.movies.domain.AddFavoriteMovieUseCase
import ru.yandex.repinanr.movies.domain.InsertCommentMovieUseCase
import ru.yandex.repinanr.movies.domain.RemoveFavoriteMovieUseCase

class SaveDialogViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MoviesListRepositoryImpl
    private val context = getApplication<Application>().applicationContext
    private val addFavoriteMovieUseCase = AddFavoriteMovieUseCase(repository)
    private val removeFavoriteMovieUseCase = RemoveFavoriteMovieUseCase(repository)
    private val insertCommentMovieUseCase = InsertCommentMovieUseCase(repository)

    fun addDeleteFavoriteMovieDB(movie: DataModel.Movie, oldIsFavorite: Boolean) {
        if (movie.isFavorite != oldIsFavorite) {
            if (oldIsFavorite) {
                deleteFavoriteMovie(movie.movieId)
            } else {
                addFavoriteMovie(movie)
            }
        }
    }

    fun addFavoriteMovie(movie: DataModel.Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            addFavoriteMovieUseCase.addFavoriteMovie(movie, context)
        }
    }

    fun deleteFavoriteMovie(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            removeFavoriteMovieUseCase.removeFavoriteMovie(id, context)
        }
    }

    fun saveMovieComment(movieId: Int, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            insertCommentMovieUseCase.insertMovieComment(movieId, comment, context)
        }
    }
}