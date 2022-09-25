package ru.yandex.repinanr.movies.presentation.moviesDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.repository.MoviesListRepositoryImpl
import ru.yandex.repinanr.movies.domain.*
import ru.yandex.repinanr.movies.presentation.common.ApiCodeConst.LIMIT
import ru.yandex.repinanr.movies.presentation.common.ApiCodeConst.NOT_FOUND
import ru.yandex.repinanr.movies.presentation.common.ApiCodeConst.TOO_MANY_RESPONSES
import java.net.UnknownHostException

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MoviesListRepositoryImpl
    private val context = getApplication<Application>().applicationContext
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

    private var isFavorite = false

    private val getMovieUseCase = GetMovieUseCase(repository)
    private val getFavoriteMovieUseCase = GetFavoriteMovieUseCase(repository)
    private val addFavoriteMovieUseCase = AddFavoriteMovieUseCase(repository)
    private val removeFavoriteMovieUseCase = RemoveFavoriteMovieUseCase(repository)
    private val getCommentMovieUseCase = GetCommentMovieUseCase(repository)
    private val insertCommentMovieUseCase = InsertCommentMovieUseCase(repository)
    private val updateCommentMovieUseCase = UpdateCommentMovieUseCase(repository)

    fun getMovie(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = getMovieUseCase.getMovie(id)

                if (response.isSuccessful) {
                    response.body()?.let { movie ->
                        val moviesItemTmp = DataModel.Movie(
                            movieId = movie.id,
                            name = movie.name ?: movie.nameEn ?: movie.nameOriginal ?: "No name",
                            description = movie.description ?: "",
                            imageUrl = movie.previewUrl,
                            isFavorite = isFavorite
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
                    _errorMessage.postValue(context.getString(error))
                    _loading.value = false
                }
            } catch (e: HttpException) {
                _errorMessage.postValue(context.getString(R.string.internet_erorr_title))
                _loading.postValue(false)
            } catch (e: UnknownHostException) {
                _errorMessage.postValue(context.getString(R.string.internet_erorr_title))
                _loading.postValue(false)
            }
        }
    }

    fun getFavoriteMovie(id: Int) {
        context.let {
            viewModelScope.launch(Dispatchers.IO) {
                isFavorite = getFavoriteMovieUseCase.getFavoriteMovie(id, context) != null
            }
        }
    }

    fun changeFavoriteMovieItem() {
        _moviesItem.postValue(moviesItem.value?.copy(isFavorite = !isFavorite))
    }

    fun addDeleteFavoriteMovieDB() {
        _moviesItem.value?.let {
            if (isFavorite != it.isFavorite) {
                if (isFavorite) {
                    deleteFavoriteMovie()
                } else {
                    addFavoriteMovie()
                }
            }
        }
    }

    fun addFavoriteMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            _moviesItem.value?.let {
                addFavoriteMovieUseCase.addFavoriteMovie(it, context)
            }
            _moviesItem.postValue(moviesItem.value?.copy(isFavorite = true))
        }
    }

    fun deleteFavoriteMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            _moviesItem.value?.let {
                removeFavoriteMovieUseCase.removeFavoriteMovie(it, context)
            }
            _moviesItem.postValue(moviesItem.value?.copy(isFavorite = false))
        }
    }

    fun saveMovieComment(movieId: Int, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (getFavoriteMovieUseCase.getFavoriteMovie(movieId, context) == null) {
                insertCommentMovieUseCase.insertMovieComment(movieId, comment, context)
            } else {
                updateCommentMovieUseCase.updateMovieComment(movieId, comment, context)
            }
        }
    }

    fun getMovieComment(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _commentMessage.postValue(getCommentMovieUseCase.getMovieComment(movieId, context))
        }
    }

    fun isMovieChanged(comment: String): Boolean {
        return comment != _commentMessage.value ||
                isFavorite != (moviesItem.value?.isFavorite ?: false)
    }
}