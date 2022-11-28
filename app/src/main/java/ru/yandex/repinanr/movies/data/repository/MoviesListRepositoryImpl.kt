package ru.yandex.repinanr.movies.data.repository

import android.annotation.SuppressLint
import androidx.paging.*
import androidx.paging.rxjava2.flowable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import ru.yandex.repinanr.movies.data.Const.DEFAULT_PAGE_SIZE
import ru.yandex.repinanr.movies.data.model.DataModel.Movie
import ru.yandex.repinanr.movies.data.model.MovieMapper
import ru.yandex.repinanr.movies.data.model.MovieRemoteMediator
import ru.yandex.repinanr.movies.data.network.service.MoviesService
import ru.yandex.repinanr.movies.data.room.*
import ru.yandex.repinanr.movies.domain.MoviesListRepository
import javax.inject.Inject

class MoviesListRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val commentsDao: CommentsDao,
    private val favoriteMoviesDao: FavoriteMoviesDao,
    private val service: MoviesService,
    private val mapper: MovieMapper,
    private val appDb: AppDb
) : MoviesListRepository {
    override fun getMovieItem(id: Int) =
        mapper.mapFlowableResponseToFlowableMovie(service.getMovie(id), false)

    override fun getMoviesList(page: Int) = service.getMovies(page)

    override fun getFavoriteMoviesList(): Observable<List<Movie>> =
        mapper.mapObservableFavoriteListToObservableMovieList(favoriteMoviesDao.getAllMovies())

    override fun getAllFavoriteMoviesSingle(): Single<List<FavoriteMovieEntity>> =
        favoriteMoviesDao.getAllMoviesSingle()

    override fun getIsFavoriteMovie(movieId: Int): Flowable<Boolean> =
        favoriteMoviesDao.getMovie(movieId).map { it.isNotEmpty() }

    override fun removeFavoriteMovie(movieId: Int) = favoriteMoviesDao.delete(movieId)

    override fun addFavoriteMovie(movie: Movie): Completable =
        favoriteMoviesDao.insert(mapper.mapMovieToEntity(movie))

    @SuppressLint("CheckResult")
    override fun getMovieComment(movieId: Int): Flowable<String>  = commentsDao.getComment(movieId)
        .map { if(it.isEmpty()) "" else it.first().comment }

    override fun setMovieComment(id: Int, comment: String) =
        commentsDao.insert(mapper.mapStringToComment(id, comment))

    @OptIn(ExperimentalPagingApi::class)
    override fun letMoviesList(): Flowable<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true),
            remoteMediator = MovieRemoteMediator(
                service = service,
                moviesDao = moviesDao,
                favoriteMoviesDao = favoriteMoviesDao,
                mapper = mapper,
                appDb = appDb
            ),
            pagingSourceFactory = { moviesDao.getAllMovies() }
        ).flowable.map { pagingData ->
            pagingData.map {
                mapper.mapEntityToMovie(it)
            }
        }
    }
}