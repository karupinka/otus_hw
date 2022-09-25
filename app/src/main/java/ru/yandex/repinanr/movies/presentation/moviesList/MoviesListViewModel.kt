package ru.yandex.repinanr.movies.presentation.moviesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.repository.MoviesListRepositoryImpl
import ru.yandex.repinanr.movies.domain.AddFavoriteMovieUseCase
import ru.yandex.repinanr.movies.domain.RemoveFavoriteMovieUseCase

class MoviesListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MoviesListRepositoryImpl
    private val context = getApplication<Application>().applicationContext

    private var _moviesList = MutableLiveData<PagingData<DataModel.Movie>>()
    val moviesList: LiveData<PagingData<DataModel.Movie>>
        get() = _moviesList

    private val removeFavoriteMovieUseCase = RemoveFavoriteMovieUseCase(repository)
    private val addFavoriteMovieUseCase = AddFavoriteMovieUseCase(repository)

    fun addFavoriteMovie(movie: DataModel.Movie) {
        context?.let {
            viewModelScope.launch(Dispatchers.IO) {
                addFavoriteMovieUseCase.addFavoriteMovie(movie, it)
                _moviesList.value?.map { if (it.movieId == movie.movieId) it.copy(isFavorite = true) else it }
                    ?.let {
                        _moviesList.postValue(it)
                    }
            }
        }
    }

    fun fetchMoviesListLiveData() {
        _moviesList = (repository.fetchMoviesList(context).cachedIn(viewModelScope)
                as? MutableLiveData<PagingData<DataModel.Movie>>)
            ?: MutableLiveData<PagingData<DataModel.Movie>>()
    }

    fun fetchMoviesListLiveDataMediator() {
        _moviesList = (repository.letFlowDb(context = context).cachedIn(viewModelScope)
                as? MutableLiveData<PagingData<DataModel.Movie>>)
            ?: MutableLiveData<PagingData<DataModel.Movie>>()
    }

    fun deleteFavoriteMovie(movie: DataModel.Movie) {
        context?.let {
            viewModelScope.launch(Dispatchers.IO) {
                removeFavoriteMovieUseCase.removeFavoriteMovie(movie, it)
                _moviesList.value?.map { if (it.movieId == movie.movieId) it.copy(isFavorite = false) else it }
                    ?.let {
                        _moviesList.postValue(it)
                    }
            }
        }
    }

    fun changeMovie(newMovie: DataModel.Movie) {
        _moviesList.value?.map { if (it.movieId == newMovie.movieId) newMovie.copy(description = "") else it }
            ?.let {
                _moviesList.postValue(it)
            }
    }
}
