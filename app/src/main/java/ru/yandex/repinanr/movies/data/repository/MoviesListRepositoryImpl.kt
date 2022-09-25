package ru.yandex.repinanr.movies.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.*
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.data.Const.DEFAULT_PAGE_SIZE
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.model.MovieRemoteMediator
import ru.yandex.repinanr.movies.data.room.CommentsEntity
import ru.yandex.repinanr.movies.data.room.Db
import ru.yandex.repinanr.movies.data.room.FavoriteMovieEntity
import ru.yandex.repinanr.movies.domain.MoviesListRepository

object MoviesListRepositoryImpl : MoviesListRepository {
    override suspend fun getMovieItem(id: Int) = App.instance.movieService.getMovie(id)

    override suspend fun getMoviesList(page: Int) = App.instance.movieService.getMovies(page)

    override suspend fun getFavoriteMoviesList(context: Context): List<FavoriteMovieEntity>? =
        Db.getInstance(context)?.getFavoriteMovieDao()?.getAllMovies()

    override suspend fun getFavoriteMovie(movieId: Int, context: Context): FavoriteMovieEntity? =
        Db.getInstance(context)?.getFavoriteMovieDao()?.getMovie(movieId)

    override suspend fun removeFavoriteMovie(movie: FavoriteMovieEntity, context: Context) {
        Db.getInstance(context)?.getFavoriteMovieDao()?.delete(movie)
    }

    override suspend fun addFavoriteMovie(movie: FavoriteMovieEntity, context: Context) {
        Db.getInstance(context)?.getFavoriteMovieDao()?.insert(movie)
    }

    override suspend fun getMovieComment(movieId: Int, context: Context): CommentsEntity? =
        Db.getInstance(context)?.getCommentsDao()?.getMovie(movieId)

    override suspend fun setMovieComment(commentsEntity: CommentsEntity, context: Context) {
        Db.getInstance(context)?.getCommentsDao()?.insert(commentsEntity)
    }

    override suspend fun updateMovieComment(commentsEntity: CommentsEntity, context: Context) {
        Db.getInstance(context)?.getCommentsDao()?.update(commentsEntity)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun letMoviesList(
        context: Context
    ): LiveData<PagingData<DataModel.Movie>> {
        val appDatabase = Db.getInstance(context)
        if (appDatabase == null) throw IllegalStateException("Database is not initialized")

        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true),
            remoteMediator = MovieRemoteMediator(
                service = App.instance.movieService,
                appDatabase = appDatabase
            ),
            pagingSourceFactory = { appDatabase.getMovieDao().getAllMovies() }
        ).liveData.map { pagingData ->
            pagingData.map {
                DataModel.Movie(
                    movieId = it.serviceId,
                    name = it.name,
                    imageUrl = it.imageUrl,
                    description = it.description,
                    isFavorite = it.isFavoriteMovie == 1
                )
            }
        }
    }
}