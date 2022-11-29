package ru.yandex.repinanr.movies.data.model

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.yandex.repinanr.movies.data.network.service.MoviesService
import ru.yandex.repinanr.movies.data.room.AppDb
import ru.yandex.repinanr.movies.data.room.FavoriteMoviesDao
import ru.yandex.repinanr.movies.data.room.MovieEntity
import ru.yandex.repinanr.movies.data.room.MoviesDao
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    val service: MoviesService,
    private val moviesDao: MoviesDao,
    private val favoriteMoviesDao: FavoriteMoviesDao,
    private val mapper: MovieMapper,
    private val appDb: AppDb
) : RxRemoteMediator<Int, MovieEntity>() {
    private var pageIndex = 1

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): Single<MediatorResult> {
        pageIndex =
            getPageIndex(loadType) ?: return Single.just(
                MediatorResult.Success(
                    endOfPaginationReached = true
                )
            )
        when (loadType) {
            LoadType.REFRESH -> pageIndex = 1
            LoadType.PREPEND -> Single.just(MediatorResult.Success(true))
            LoadType.APPEND -> {
                var lastItem = state.lastItemOrNull()

                if (lastItem == null) {
                    pageIndex = INVALID_PAGE
                    return Single.just(MediatorResult.Success(true))
                }
            }

        }

        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .flatMap {
                if (pageIndex == INVALID_PAGE) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    service.getMovies(pageIndex)
                        .zipWith(favoriteMoviesDao.getAllMoviesSingle()) { movie, favorite ->
                            mapper.mapListResponseToListEntity(movie.items, favorite)
                        }
                        .map {
                            insertData(loadType, it)
                        }
                        .map<MediatorResult> { MediatorResult.Success(endOfPaginationReached = pageIndex == INVALID_PAGE) }
                        .onErrorReturn { MediatorResult.Error(it) }
                }
            }
            .onErrorReturn { MediatorResult.Error(it) }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }


    private fun insertData(loadType: LoadType, data: List<MovieEntity>): List<MovieEntity> {
        appDb.runInTransaction{
            if (loadType == LoadType.REFRESH) {
                moviesDao.removeMovies()
            }

            moviesDao.insertAll(data)
        }

        return data
    }

        companion object {
            private const val TAG = "MovieRemoteMediator"
            private const val INVALID_PAGE = -1
        }
    }
