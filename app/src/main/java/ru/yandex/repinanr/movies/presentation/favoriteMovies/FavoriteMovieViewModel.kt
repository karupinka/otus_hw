package ru.yandex.repinanr.movies.presentation.favoriteMovies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.model.DataModel.Movie
import ru.yandex.repinanr.movies.domain.AddFavoriteMovieUseCase
import ru.yandex.repinanr.movies.domain.GetFavoriteMovieListUseCase
import ru.yandex.repinanr.movies.domain.RemoveFavoriteMovieUseCase
import javax.inject.Inject

class FavoriteMovieViewModel @Inject constructor(
    private val getFavoriteMovieListUseCase: GetFavoriteMovieListUseCase,
    private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
    private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase
) : ViewModel() {
    private val _moviesList = MutableLiveData<List<Movie>>(listOf())
    val moviesList: LiveData<List<Movie>>
        get() = _moviesList

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int>
        get() = _errorMessage

    private var movieRemoved: Movie? = null
    private val mDisposable = CompositeDisposable()

    fun getFavoriteMovies() {
        mDisposable.add(
            getFavoriteMovieListUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                _moviesList.postValue(response)
                Log.d(TAG, response.toString())
            }, { failure ->
                _errorMessage.postValue(R.string.other_erorr_title)
                Log.d(TAG, failure.message ?: "Empty message error")
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.dispose()
    }

    fun removeFavoriteMovie(movie: Movie) {
        val currentList = moviesList.value?.toMutableList()

        movieRemoved = movie
        mDisposable.add(
            removeFavoriteMovieUseCase(movie.movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    currentList?.let {
                        it.remove(movie)
                        _moviesList.postValue(it)
                    }
                }, { failure ->
                    _errorMessage.postValue(R.string.other_erorr_title)
                    Log.d(TAG, failure.message ?: "Empty message error")
                })
        )
    }

    fun cancelFavoriteMoviesRemove(position: Int) {
        movieRemoved?.let { movieRemoved ->
            mDisposable.add(
                addFavoriteMovieUseCase(movieRemoved)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val currentList = _moviesList.value?.toMutableList()
                        currentList?.let {
                            it.add(position, movieRemoved)
                            _moviesList.postValue(it)
                        }
                    }, { failure ->
                        _errorMessage.postValue(R.string.other_erorr_title)
                        Log.d(TAG, failure.message ?: "Empty message error")
                    })
            )
        }
    }

    companion object {
        private const val TAG = "FavoriteMovieViewModel"
    }
}