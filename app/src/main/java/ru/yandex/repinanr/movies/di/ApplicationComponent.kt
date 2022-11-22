package ru.yandex.repinanr.movies.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.yandex.repinanr.movies.presentation.dialog.SaveDataDialog
import ru.yandex.repinanr.movies.presentation.favoriteMovies.FavoriteMovieFragment
import ru.yandex.repinanr.movies.presentation.moviesDetails.MoviesDetailFragment
import ru.yandex.repinanr.movies.presentation.moviesList.MoviesListFragment

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(fragment: MoviesListFragment)

    fun inject(fragment: FavoriteMovieFragment)

    fun inject(fragment: MoviesDetailFragment)

    fun inject(fragment: SaveDataDialog)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}