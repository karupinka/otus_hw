package ru.yandex.repinanr.movies

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.model.MovieMapper
import ru.yandex.repinanr.movies.data.model.MovieResponse
import ru.yandex.repinanr.movies.data.room.CommentsEntity
import ru.yandex.repinanr.movies.data.room.FavoriteMovieEntity
import ru.yandex.repinanr.movies.data.room.MovieEntity

@RunWith(MockitoJUnitRunner::class)
class MoviesMapperUnitTest {

    lateinit var movieMapper: MovieMapper

    @Before
    fun setUp() {
        movieMapper = MovieMapper()
    }

    @Test
    fun checkCommentMap() {
        Assert.assertEquals(
            movieMapper.mapStringToComment(12, "Test"), buildCommentEntity()
        )
    }

    @Test
    fun checkMapEntityToMovie() {
        val movie = movieMapper.mapEntityToMovie(buildMovieEntity())

        Assert.assertEquals(
            movie::class.java,
            DataModel.Movie::class.java
        )

        Assert.assertEquals(movie, buildMovie())
    }

    @Test
    fun checkMapMovieToEntity() {
        val entity = movieMapper.mapMovieToEntity(buildMovie())

        Assert.assertEquals(
            entity::class.java,
            FavoriteMovieEntity::class.java
        )

        Assert.assertEquals(entity, buildFavoriteMovie())
    }

    @Test
    fun checkMapFavoriteMovieToMovie() {
        val movie = movieMapper.mapFavoriteMovieToMovie(buildFavoriteMovie())

        Assert.assertEquals(
            movie::class.java,
            DataModel.Movie::class.java
        )

        Assert.assertEquals(movie, buildMovie())
    }

    @Test
    fun checkMapResponseToMovieOriginal() {
        val movie =
            movieMapper.mapResponseToMovie(buildMovieResponseWithoutNameAndEn(), isFavorite = false)

        Assert.assertEquals(
            movie::class.java,
            DataModel.Movie::class.java
        )

        Assert.assertEquals(movie.name, "nameOriginal")
    }

    @Test
    fun checkMapResponseToMovieEn() {
        val movie =
            movieMapper.mapResponseToMovie(buildMovieResponseWithoutName(), isFavorite = false)

        Assert.assertEquals(movie.name, "nameEn")
    }

    @Test
    fun checkMapResponseToMovie() {
        val movie =
            movieMapper.mapResponseToMovie(buildMovieResponse(), isFavorite = true)

        Assert.assertEquals(movie, buildMovie())
    }

    companion object {
        fun buildCommentEntity() = CommentsEntity(
            serviceId = 12,
            comment = "Test"
        )

        fun buildMovieEntity() = MovieEntity(
            serviceId = 12,
            name = "Test",
            description = "description",
            isFavoriteMovie = 1,
            imageUrl = ""
        )

        fun buildMovieResponseWithoutNameAndEn() = MovieResponse(
            id = 12,
            name = null,
            nameOriginal = "nameOriginal",
            nameEn = null,
            description = "description",
            previewUrl = ""
        )

        fun buildMovieResponseWithoutName() = MovieResponse(
            id = 12,
            name = null,
            nameOriginal = "nameOriginal",
            nameEn = "nameEn",
            description = "description",
            previewUrl = ""
        )

        fun buildMovieResponse() = MovieResponse(
            id = 12,
            name = "Test",
            nameOriginal = "nameOriginal",
            nameEn = "nameEn",
            description = "description",
            previewUrl = ""
        )

        fun buildMovie() = DataModel.Movie(
            movieId = 12,
            name = "Test",
            description = "description",
            isFavorite = true,
            imageUrl = ""
        )

        fun buildFavoriteMovie() = FavoriteMovieEntity(
            serviceId = 12,
            name = "Test",
            description = "description",
            imageUrl = ""
        )
    }
}