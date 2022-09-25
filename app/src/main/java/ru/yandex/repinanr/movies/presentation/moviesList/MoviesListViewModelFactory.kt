package ru.yandex.repinanr.movies.presentation.moviesList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoviesListViewModelFactory(val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoviesListViewModel(application) as T
    }
}