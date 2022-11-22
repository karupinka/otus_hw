package ru.yandex.repinanr.movies.presentation.moviesDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.repository.MoviesListRepositoryImpl
import ru.yandex.repinanr.movies.domain.GetCommentMovieUseCase
import ru.yandex.repinanr.movies.domain.GetFavoriteMovieUseCase
import ru.yandex.repinanr.movies.domain.GetMovieUseCase
import ru.yandex.repinanr.movies.presentation.common.ApiCodeConst.LIMIT
import ru.yandex.repinanr.movies.presentation.common.ApiCodeConst.NOT_FOUND
import ru.yandex.repinanr.movies.presentation.common.ApiCodeConst.TOO_MANY_RESPONSES
import java.net.UnknownHostException

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MoviesListRepositoryImpl
    private var _moviesItem = MutableLiveData<DataModel.Movie>()
    val moviesItem: LiveData<DataModel.Movie>
        get() = _moviesItem

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _commentMessage = MutableLiveData<String>()
    val commentMessage: LiveData<String>
        get() = _commentMessage

    private var _isFavorite = false
    val isFavorite: Boolean
        get() = _isFavorite

    private val getMovieUseCase = GetMovieUseCase(repository)
    private val getFavoriteMovieUseCase = GetFavoriteMovieUseCase(repository)
    private val getCommentMovieUseCase = GetCommentMovieUseCase(repository)

    fun getMovie(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = getMovieUseCase.getMovie(id)

                if (response.isSuccessful) {
                    response.body()?.let { movie ->
                        val moviesItemTmp = DataModel.Movie(
                            movieId = movie.id,
                            name = movie.name ?: movie.nameEn ?: movie.nameOriginal ?: "No name",
                            description = movie.description ?: "",
                            imageUrl = movie.previewUrl,
                            isFavorite = _isFavorite
                        )
                        _moviesItem.postValue(moviesItemTmp)
                        _loading.postValue(false)
                    }
                } else {
                    val error = when (response.code()) {
                        NOT_FOUND -> R.string.not_found_error_title
                        LIMIT, TOO_MANY_RESPONSES -> R.string.limit_erorr_title
                        else -> R.string.other_erorr_title
                    }
                    _errorMessage.postValue(
                        getApplication<Application>().applicationContext.getString(error)
                    )
                    _loading.value = false
                }
            } catch (e: HttpException) {
                _errorMessage.postValue(
                    getApplication<Application>().applicationContext.getString(R.string.other_erorr_title)
                )
                _loading.postValue(false)
            } catch (e: UnknownHostException) {
                _errorMessage.postValue(
                    getApplication<Application>().applicationContext.getString(R.string.other_erorr_title)
                )
                _loading.postValue(false)
            }
        }
    }

    fun getFavoriteMovie(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isFavorite = getFavoriteMovieUseCase.getFavoriteMovie(id, getApplication()) != null
        }
    }

    fun changeFavoriteMovieItem() {
        _moviesItem.postValue(moviesItem.value?.copy(isFavorite = !_isFavorite))
    }

    fun getMovieComment(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _commentMessage.postValue(
                getCommentMovieUseCase.getMovieComment(
                    movieId,
                    getApplication()
                )
            )
        }
    }

    fun isMovieChanged(comment: String): Boolean {
        return comment != _commentMessage.value ||
                _isFavorite != (moviesItem.value?.isFavorite ?: false)
    }
}