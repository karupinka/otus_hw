package ru.yandex.repinanr.movies

import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.domain.AddFavoriteMovieUseCase
import ru.yandex.repinanr.movies.domain.GetFavoriteMovieListUseCase
import ru.yandex.repinanr.movies.domain.RemoveFavoriteMovieUseCase
import ru.yandex.repinanr.movies.presentation.favoriteMovies.FavoriteMovieViewModel
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class MoviesFavoriteUnitTest {

    @Mock
    lateinit var addFavoriteMovieUseCase: AddFavoriteMovieUseCase

    @Mock
    lateinit var getFavoriteMovieListUseCase: GetFavoriteMovieListUseCase

    @Mock
    lateinit var removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase

    lateinit var favoriteMovieViewModel: FavoriteMovieViewModel


    @Before
    fun setUp() {
        favoriteMovieViewModel = FavoriteMovieViewModel(
            getFavoriteMovieListUseCase,
            addFavoriteMovieUseCase,
            removeFavoriteMovieUseCase
        )
    }

    @Test
    fun checkGetFavoriteMovieListUseCaseError() {
        Mockito.`when`(getFavoriteMovieListUseCase.invoke())
            .thenReturn(Observable.error(IOException()))

        favoriteMovieViewModel.getFavoriteMovies()

        verify(getFavoriteMovieListUseCase).invoke()
        assert(favoriteMovieViewModel.errorMessage.value == R.string.other_erorr_title)
    }

    @Test
    fun checkGetFavoriteMovieListSucceess() {
        Mockito.`when`(getFavoriteMovieListUseCase.invoke())
            .thenReturn(Observable.just(
                listOf(buildMovie())))

        favoriteMovieViewModel.getFavoriteMovies()

        verify(getFavoriteMovieListUseCase).invoke()
    }

    companion object {
        fun buildMovie() = DataModel.Movie(
            movieId = 123,
            name = "Movie",
            description = "",
            imageUrl = "",
            isFavorite = false,
            comment = ""
        )
    }
}