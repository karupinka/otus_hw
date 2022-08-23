package ru.yandex.repinanr.movies.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.app.getInitialMoviesArray

class DataSource {
    private val initialMoviesList = getInitialMoviesArray()
    private val moviesLiveData = MutableLiveData(initialMoviesList)

    /**
     * Update changed movie item in LiveData array
     *
     * @param [oldMovie] Old movie item
     * @param [newMovie] New movie item
     */
    fun changeMovie(oldMovie: DataModel.Movie, newMovie: DataModel.Movie) {
        val currentList = moviesLiveData.value
        if (currentList?.contains(oldMovie) ?: false) {
            currentList?.set(App.getIndex(oldMovie.movieId), newMovie)
            moviesLiveData.value = currentList
        }
    }

    /**
     * Get movie from LiveData array
     *
     * @return Movie array from LiveData
     */
    fun getMovieArrayList(): LiveData<ArrayList<DataModel.Movie>> {
        return moviesLiveData
    }

    companion object {
        private var INSTANCE: DataSource? = null

        /**
         * Create DataSource Instance
         *
         * @return DataSource Instance
         */
        fun getDataSource(): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}
