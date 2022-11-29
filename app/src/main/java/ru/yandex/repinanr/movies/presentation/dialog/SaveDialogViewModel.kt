package ru.yandex.repinanr.movies.presentation.dialog

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.domain.AddFavoriteMovieUseCase
import ru.yandex.repinanr.movies.domain.InsertCommentMovieUseCase
import ru.yandex.repinanr.movies.domain.RemoveFavoriteMovieUseCase
import javax.inject.Inject

class SaveDialogViewModel @Inject constructor(
    private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
    private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase,
    private val insertCommentMovieUseCase: InsertCommentMovieUseCase
) : ViewModel() {
    private val mDisposable = CompositeDisposable()

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
        mDisposable.add(addFavoriteMovieUseCase(movie)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "addFavoriteMovie success")
            }, {
                Log.d(TAG, "addFavoriteMovie error ${it.message}")
            })
        )
    }

    fun deleteFavoriteMovie(id: Int) {
        mDisposable.add(
            removeFavoriteMovieUseCase(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "deleteFavoriteMovie success")
                }, {
                    Log.d(TAG, "deleteFavoriteMovie error ${it.message}")
                })
        )
    }

    fun saveMovieComment(movieId: Int, comment: String) {
        mDisposable.add(
            insertCommentMovieUseCase(movieId, comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "saveMovieComment success")
                }, {
                    Log.d(TAG, "saveMovieComment error ${it.message}")
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.dispose()
    }

    companion object {
        private const val TAG = "SaveDialogViewModel"
    }
}