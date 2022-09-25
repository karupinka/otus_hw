package ru.yandex.repinanr.movies.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = arrayOf(
        MovieEntity::class,
        CommentsEntity::class,
        FavoriteMovieEntity::class
    ), version = 4
)
abstract class AppDb : RoomDatabase() {
    abstract fun getMovieDao(): MoviesDao

    abstract fun getCommentsDao(): CommentsDao

    abstract fun getFavoriteMovieDao(): FavoriteMoviesDao
}