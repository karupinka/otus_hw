package ru.yandex.repinanr.movies.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.yandex.repinanr.movies.presentation.dialog.SaveDialogViewModel
import ru.yandex.repinanr.movies.presentation.favoriteMovies.FavoriteMovieViewModel
import ru.yandex.repinanr.movies.presentation.moviesDetails.MovieDetailViewModel
import ru.yandex.repinanr.movies.presentation.moviesList.MoviesListViewModel

@Module
interface ViewModelModule {

    @Binds
    fun bindsMovieListViewModel(model: MoviesListViewModel): ViewModel

    @Binds
    fun bindsFavoriteMoviesViewModel(model: FavoriteMovieViewModel): ViewModel

    @Binds
    fun bindsMovieDetailsViewModel(model: MovieDetailViewModel): ViewModel

    @Binds
    fun bindsSaveDialogViewModel(model: SaveDialogViewModel): ViewModel
}