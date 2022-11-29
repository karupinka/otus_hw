package ru.yandex.repinanr.movies.data.model

import io.reactivex.Flowable
import io.reactivex.Observable
import ru.yandex.repinanr.movies.data.model.DataModel.Movie
import ru.yandex.repinanr.movies.data.room.CommentsEntity
import ru.yandex.repinanr.movies.data.room.FavoriteMovieEntity
import ru.yandex.repinanr.movies.data.room.MovieEntity
import javax.inject.Inject

class MovieMapper @Inject constructor() {

    fun mapResponseToMovie(response: MovieResponse, isFavorite: Boolean) = Movie(
        movieId = response.id,
        name = response.name ?: response.nameEn ?: response.nameOriginal ?: "No name",
        description = response.description ?: "",
        imageUrl = response.previewUrl,
        isFavorite = isFavorite
    )

    fun mapEntityToMovie(entity: MovieEntity) = Movie(
        movieId = entity.serviceId,
        name = entity.name,
        description = entity.description,
        imageUrl = entity.imageUrl,
        isFavorite = entity.isFavoriteMovie == 1
    )

    fun mapMovieToEntity(movie: Movie) = FavoriteMovieEntity(
        serviceId = movie.movieId,
        name = movie.name,
        description = movie.description,
        imageUrl = movie.imageUrl ?: ""
    )

    fun mapFlowableResponseToFlowableMovie(response: Flowable<MovieResponse>, isFavorite: Boolean) =
        response.map {
            mapResponseToMovie(it, isFavorite)
        }

    fun mapFavoriteMovieToMovie(favoriteMovieEntity: FavoriteMovieEntity) =
        Movie(
            movieId = favoriteMovieEntity.serviceId,
            name = favoriteMovieEntity.name,
            description = favoriteMovieEntity.description,
            imageUrl = favoriteMovieEntity.imageUrl,
            isFavorite = true
        )

    fun mapFlowableFavoriteMovieToMovie(favoriteMovieEntity: Flowable<FavoriteMovieEntity>) =
        favoriteMovieEntity.map {
            mapFavoriteMovieToMovie(it)
        }

    fun mapListResponseToListMovie(movieResponses: List<MovieResponse>, favoriteMovies: List<Int>) =
        movieResponses.map {
            mapResponseToMovie(it, favoriteMovies.contains(it.id))
        }

    fun mapListResponseToListEntity(
        movieResponses: List<MovieResponse>,
        favoriteMovies: List<FavoriteMovieEntity>
    ): List<MovieEntity> {
        val mappedFavoriteMovies = favoriteMovies.map { it.serviceId }
        return movieResponses.map { movie ->
            MovieEntity(
                serviceId = movie.id,
                name = movie.name ?: movie.nameEn ?: movie.nameOriginal ?: "No name",
                description = movie.description ?: "",
                imageUrl = movie.previewUrl,
                isFavoriteMovie = if(mappedFavoriteMovies.contains(movie.id)) 1 else 0
            )
        }
    }

    fun mapFavoriteListToMovieList(
        favoriteMovieResponses: List<FavoriteMovieEntity>
    ) = favoriteMovieResponses.map {
        mapFavoriteMovieToMovie(it)
    }

    fun mapObservableFavoriteListToObservableMovieList(
        favoriteMovieResponses: Observable<List<FavoriteMovieEntity>>
    ) = favoriteMovieResponses.map {
        mapFavoriteListToMovieList(it)
    }

    fun mapStringToComment(movieId: Int, comment: String) = CommentsEntity(movieId, comment)
}
