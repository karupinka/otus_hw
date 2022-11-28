package ru.yandex.repinanr.movies.di

import dagger.Binds
import dagger.Module
import ru.yandex.repinanr.movies.data.repository.MoviesListRepositoryImpl
import ru.yandex.repinanr.movies.domain.MoviesListRepository

@Module
interface RepositoryModule {

    @Binds
    @ApplicationScope
    fun bindMovieRepository(moviesListRepositoryImpl: MoviesListRepositoryImpl): MoviesListRepository
}