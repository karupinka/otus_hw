package ru.yandex.repinanr.movies.data.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.yandex.repinanr.movies.data.network.service.MoviesService
import ru.yandex.repinanr.movies.data.room.AppDb
import java.io.IOException

private const val START_PAGE_INDEX = 1

class MovieDataSource(
    private val service: MoviesService,
    private val appDatabase: AppDb?
) : PagingSource<Int, DataModel.Movie>() {
    override fun getRefreshKey(state: PagingState<Int, DataModel.Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataModel.Movie> {
        val page = params.key ?: START_PAGE_INDEX

        return try {
            val moviesList = mutableListOf<DataModel.Movie>()
            val response = service.getMovies(page)
            val movies = response.body()?.items ?: listOf<MovieResponse>()
            val favoriteMoviesId =
                appDatabase?.getFavoriteMovieDao()?.getAllMovies()?.map { it.serviceId }
            movies.forEach { movie ->
                val movieTmp = DataModel.Movie(
                    movieId = movie.id,
                    name = movie.name ?: movie.nameEn ?: movie.nameOriginal
                    ?: "No name",
                    description = movie.description ?: "",
                    imageUrl = movie.previewUrl,
                    isFavorite = favoriteMoviesId?.contains(movie.id) ?: false
                )

                moviesList.add(movieTmp)
            }

            LoadResult.Page(
                data = moviesList,
                prevKey = if (page == START_PAGE_INDEX) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}