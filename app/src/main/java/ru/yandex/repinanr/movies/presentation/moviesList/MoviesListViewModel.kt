package ru.yandex.repinanr.movies.presentation.moviesList

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava2.cachedIn
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.model.DataModel.Movie
import ru.yandex.repinanr.movies.domain.AddFavoriteMovieUseCase
import ru.yandex.repinanr.movies.domain.LetMovieUseCase
import ru.yandex.repinanr.movies.domain.RemoveFavoriteMovieUseCase
import ru.yandex.repinanr.movies.presentation.common.ApiCodeConst
import java.net.UnknownHostException
import javax.inject.Inject

class MoviesListViewModel @Inject constructor(
    private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase,
    private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
    private val letMovieUseCase: LetMovieUseCase
) : ViewModel() {

    private var _moviesList = MutableLiveData<PagingData<Movie>>()
    val moviesList: LiveData<PagingData<Movie>>
        get() = _moviesList
    private val _loading = MutableLiveData<Boolean>(true)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int>
        get() = _errorMessage

    private val mDisposable = CompositeDisposable()

    fun addFavoriteMovie(movie: Movie) {
        mDisposable.add(
            addFavoriteMovieUseCase(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _moviesList.value?.map { if (it.movieId == movie.movieId) it.copy(isFavorite = true) else it }
                        ?.let {
                            _moviesList.postValue(it)
                        }
                }, {
                    _errorMessage.postValue(R.string.other_erorr_title)
                    Log.d(TAG, "addFavoriteMovieUseCase error")
                })
        )
    }

    @SuppressLint("CheckResult")
    fun fetchMoviesListLiveDataMediator() {
        letMovieUseCase()
            .cachedIn(viewModelScope)
            .subscribe ({
                onRetrieveArticleListFinish()
                _moviesList.postValue(it)
            }, { failure ->
                onRetrieveArticleListFinish()
                _errorMessage.postValue(
                    when (failure) {
                        is HttpException -> {
                            when (failure.response()!!.code()) {
                                ApiCodeConst.NOT_FOUND -> R.string.not_found_error_title
                                ApiCodeConst.LIMIT, ApiCodeConst.TOO_MANY_RESPONSES -> R.string.limit_erorr_title
                                else -> R.string.other_erorr_title
                            }
                        }
                        is UnknownHostException -> R.string.internet_erorr_title
                        else -> R.string.other_erorr_title
                    }
                )
                Log.d(TAG, "fetchMoviesListLiveDataMediator error")
            })
    }

    fun deleteFavoriteMovie(movie: Movie) {
        mDisposable.add(
            removeFavoriteMovieUseCase(movie.movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _moviesList.value?.map { if (it.movieId == movie.movieId) it.copy(isFavorite = false) else it }
                    ?.let {
                        _moviesList.postValue(it)
                    }
            }, { failure ->
                _errorMessage.postValue(R.string.other_erorr_title)
                Log.d(TAG, "deleteFavoriteMovie error")
            })
        )
    }

    fun changeMovie(newMovie: Movie) {
        _moviesList.value?.map { if (it.movieId == newMovie.movieId) newMovie.copy(description = "") else it }
            ?.let {
                _moviesList.postValue(it)
            }
    }

    private fun onRetrieveArticleListFinish() {
        _loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.dispose()
    }

    companion object {
        private const val TAG = "MoviesListViewModel"
    }
}
