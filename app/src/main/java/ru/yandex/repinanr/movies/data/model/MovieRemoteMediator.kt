package ru.yandex.repinanr.movies.data.model

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.yandex.repinanr.movies.data.network.service.MoviesService
import ru.yandex.repinanr.movies.data.room.AppDb
import ru.yandex.repinanr.movies.data.room.MovieEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    val service: MoviesService,
    val appDatabase: AppDb
) : RemoteMediator<Int, MovieEntity>() {
    private var pageIndex = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        pageIndex =
            getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            val response = service.getMovies(pageIndex)

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.getMovieDao().removeMovies()
                }
                val movies = mutableListOf<MovieEntity>()
                val favoriteMovies =
                    appDatabase.getFavoriteMovieDao().getAllMovies().map { it.serviceId }
                response.body()?.items?.forEach { movieResponse ->
                    movies.add(
                        MovieEntity(
                            serviceId = movieResponse.id,
                            name = movieResponse.name ?: movieResponse.nameOriginal
                            ?: movieResponse.nameEn ?: "",
                            description = movieResponse.description ?: "",
                            imageUrl = movieResponse.previewUrl,
                            isFavoriteMovie = if (favoriteMovies.contains(movieResponse.id)
                                    ?: false
                            ) 1 else 0
                        )
                    )
                }
                appDatabase.getMovieDao().insertAll(movies)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.body()?.items == null
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }
}