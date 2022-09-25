package ru.yandex.repinanr.movies.presentation.favoriteMovies

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.repository.MoviesListRepositoryImpl
import ru.yandex.repinanr.movies.data.room.Db
import ru.yandex.repinanr.movies.domain.AddFavoriteMovieUseCase
import ru.yandex.repinanr.movies.domain.RemoveFavoriteMovieUseCase

class FavoriteMovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MoviesListRepositoryImpl
    private val context = getApplication<Application>().applicationContext

    private val _moviesList = MutableLiveData<List<DataModel.Movie>>(listOf())
    val moviesList: LiveData<List<DataModel.Movie>>
        get() = _moviesList

    private var movieRemoved: DataModel.Movie? = null
    private val addFavoriteMovieUseCase = AddFavoriteMovieUseCase(repository)
    private val removeFavoriteMovieUseCase = RemoveFavoriteMovieUseCase(repository)

    fun getFavoriteMovies(context: Context) {
        viewModelScope.launch {
            val response = Db.getInstance(context)?.getFavoriteMovieDao()?.getAllMovies()
            val currentList = mutableListOf<DataModel.Movie>()
            response?.forEach {
                it?.let {
                    val movieTmp = DataModel.Movie(
                        movieId = it.serviceId,
                        name = it.name,
                        description = it.description,
                        imageUrl = it.imageUrl,
                        isFavorite = true
                    )
                    currentList?.add(movieTmp)
                }
            }
            _moviesList.postValue(currentList)
        }
    }

    fun removeFavoriteMovie(movie: DataModel.Movie) {
        viewModelScope.launch {
            movieRemoved = movie
            removeFavoriteMovieUseCase.removeFavoriteMovie(movie, context)
            val currentList = moviesList.value?.toMutableList()
            currentList?.remove(movie)
            _moviesList.postValue(currentList)
        }
    }

    fun cancelFavoriteMoviesRemove(position: Int) {
        viewModelScope.launch {
            movieRemoved?.let { movie ->
                addFavoriteMovieUseCase.addFavoriteMovie(movie, context)
                val currentList = _moviesList.value?.toMutableList()
                currentList?.add(position, movie)
                _moviesList.postValue(currentList)
            }
        }
    }
}