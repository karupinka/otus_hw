package ru.yandex.repinanr.movies.presentation.favoriteMovies

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FavoriteViewModelFactory(val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteMovieViewModel(application) as T
    }
}