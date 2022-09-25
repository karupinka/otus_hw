package ru.yandex.repinanr.movies.data.model

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.yandex.repinanr.movies.data.Const.DEFAULT_PAGE_INDEX
import ru.yandex.repinanr.movies.data.network.service.MoviesService
import ru.yandex.repinanr.movies.data.room.AppDb
import ru.yandex.repinanr.movies.data.room.MovieEntity
import ru.yandex.repinanr.movies.data.room.RemoteKeysEntity
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    val service: MoviesService,
    val appDatabase: AppDb
) : RemoteMediator<Int, MovieEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val pageKeyData = getKeyPageData(loadType, state)

        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        return try {
            val response = service.getMovies(page)
            val isEndOfList = response.body()?.items?.isEmpty() ?: true

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.getRemoteKeysDao().removeRemoteKeys()
                    appDatabase.getMovieDao().removeMovies()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.body()?.items?.map {
                    RemoteKeysEntity(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                } ?: listOf()
                val movies = mutableListOf<MovieEntity>()
                appDatabase.getRemoteKeysDao().insertAll(keys)
                response.body()?.items?.forEach { movieResponse ->
                    movies.add(
                        MovieEntity(
                            serviceId = movieResponse.id,
                            name = movieResponse.name ?: movieResponse.nameOriginal
                            ?: movieResponse.nameEn ?: "",
                            description = movieResponse.description ?: "",
                            imageUrl = movieResponse.previewUrl,
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

    private suspend fun getFirstRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie -> appDatabase.getRemoteKeysDao().remoteKeysId(movie.serviceId) }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movie -> appDatabase.getRemoteKeysDao().remoteKeysId(movie.serviceId) }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.serviceId?.let { repoId ->
                appDatabase.getRemoteKeysDao().remoteKeysId(repoId)
            }
        }
    }

    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, MovieEntity>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: throw InvalidObjectException("Invalid state, key should not be null")
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }
}