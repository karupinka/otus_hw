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

    @IntoMap
    @ViewModelKey(MoviesListViewModel::class)
    @Binds
    fun bindsMovieListViewModel(model: MoviesListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(FavoriteMovieViewModel::class)
    @Binds
    fun bindsFavoriteMoviesViewModel(model: FavoriteMovieViewModel): ViewModel

    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    @Binds
    fun bindsMovieDetailsViewModel(model: MovieDetailViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SaveDialogViewModel::class)
    @Binds
    fun bindsSaveDialogViewModel(model: SaveDialogViewModel): ViewModel
}