package ru.yandex.repinanr.movies.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.yandex.repinanr.movies.data.network.service.ApiFactory
import ru.yandex.repinanr.movies.data.network.service.MoviesService
import ru.yandex.repinanr.movies.data.repository.MoviesListRepositoryImpl
import ru.yandex.repinanr.movies.data.room.AppDb
import ru.yandex.repinanr.movies.data.room.CommentsDao
import ru.yandex.repinanr.movies.data.room.FavoriteMoviesDao
import ru.yandex.repinanr.movies.data.room.MoviesDao
import ru.yandex.repinanr.movies.domain.MoviesListRepository

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindMovieRepository(moviesListRepositoryImpl: MoviesListRepositoryImpl): MoviesListRepository

    companion object {
        @Provides
        @ApplicationScope
        fun provideMoviesDao(application: Application): MoviesDao {
            return AppDb.getInstance(application).getMovieDao()
        }

        @Provides
        @ApplicationScope
        fun provideFavoriteMoviesDao(application: Application): FavoriteMoviesDao {
            return AppDb.getInstance(application).getFavoriteMovieDao()
        }

        @Provides
        @ApplicationScope
        fun provideCommentMoviesDao(application: Application): CommentsDao {
            return AppDb.getInstance(application).getCommentsDao()
        }

        @Provides
        @ApplicationScope
        fun provideApiService(): MoviesService {
            return ApiFactory.apiService
        }

        @Provides
        @ApplicationScope
        fun provideAppDb(application: Application): AppDb {
            return AppDb.getInstance(application)
        }
    }
}