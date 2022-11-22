package ru.yandex.repinanr.movies.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = arrayOf(
        MovieEntity::class,
        CommentsEntity::class,
        FavoriteMovieEntity::class
    ),
    version = 4,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun getMovieDao(): MoviesDao

    abstract fun getCommentsDao(): CommentsDao

    abstract fun getFavoriteMovieDao(): FavoriteMoviesDao

    companion object {

        private var db: AppDb? = null
        private const val DB_NAME = "db-name.db"
        private val LOCK = Any()

        fun getInstance(context: Context): AppDb {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        context,
                        AppDb::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                db = instance
                return instance
            }
        }
    }
}