package ru.yandex.repinanr.movies.presentation.moviesDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.model.DataModel.Movie
import ru.yandex.repinanr.movies.domain.GetCommentMovieUseCase
import ru.yandex.repinanr.movies.domain.GetFavoriteMovieUseCase
import ru.yandex.repinanr.movies.domain.GetMovieUseCase
import ru.yandex.repinanr.movies.presentation.common.ApiCodeConst.LIMIT
import ru.yandex.repinanr.movies.presentation.common.ApiCodeConst.NOT_FOUND
import ru.yandex.repinanr.movies.presentation.common.ApiCodeConst.TOO_MANY_RESPONSES
import java.net.UnknownHostException
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val getFavoriteMovieUseCase: GetFavoriteMovieUseCase,
    private val getCommentMovieUseCase: GetCommentMovieUseCase
) : ViewModel() {
    private var _moviesItem = MutableLiveData<Movie>()
    val moviesItem: MutableLiveData<Movie>
        get() = _moviesItem

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int>
        get() = _errorMessage

    private val _commentMessage = MutableLiveData<String>()
    val commentMessage: LiveData<String>
        get() = _commentMessage

    private var _isFavorite = false
    val isFavorite: Boolean
        get() = _isFavorite
    private val mDisposable = CompositeDisposable()

    fun loadMovie(id: Int) {
        mDisposable.add(
            Flowable.zip(getMovieUseCase(id), getCommentMovieUseCase(id), getFavoriteMovieUseCase(id)) {
                movie, comment, favorite ->
                movie.copy(comment = comment, isFavorite = favorite)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onApiResponseStart() }
                .doOnTerminate { onRetrieveArticleListFinish() }
                .subscribe({ response ->
                    _moviesItem.postValue(response)
                    _isFavorite = response.isFavorite
                    _commentMessage.postValue(response.comment)
                    Log.d(TAG, response.toString())
                }, { failure ->
                    Log.d(TAG, failure.message ?: failure.stackTraceToString())
                    _errorMessage.postValue(
                        when (failure) {
                            is HttpException -> {
                                when (failure.response()!!.code()) {
                                    NOT_FOUND -> R.string.not_found_error_title
                                    LIMIT, TOO_MANY_RESPONSES -> R.string.limit_erorr_title
                                    else -> R.string.other_erorr_title
                                }
                            }
                            is UnknownHostException -> R.string.internet_erorr_title
                            else -> R.string.other_erorr_title
                        }
                    )
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.dispose()
    }

    private fun onApiResponseStart() {
        _loading.postValue(true)
    }

    private fun onRetrieveArticleListFinish() {
        _loading.postValue(false)
    }

    fun changeFavoriteMovieItem() {
        _moviesItem.postValue(moviesItem.value?.copy(isFavorite = !_isFavorite))
    }

    fun isMovieChanged(comment: String): Boolean {
        return comment != _commentMessage.value ||
                _isFavorite != (moviesItem.value?.isFavorite ?: false)
    }

    companion object {
        const val TAG = "MovieDetailViewModel"
    }
}