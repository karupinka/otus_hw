/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.yandex.repinanr.movies.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.yandex.repinanr.movies.app.getInitialMoviesArray

class DataSource {
    private val initialMoviesList = getInitialMoviesArray()
    private val moviesLiveData = MutableLiveData(initialMoviesList)

    /**
     * Update changed movie item in LiveData array
     *
     * @param [index] Choose movie id
     * @param [comment] Choose movie id
     * @param [isFavorite] Choose movie id
     */
    fun changeMovie(index: Int, comment: String, isFavorite: Boolean) {
        val currentList = moviesLiveData.value
        if (currentList != null && currentList.size >= index) {
            currentList[index].isFavorite = isFavorite
            currentList[index].comment = comment
            moviesLiveData.postValue(currentList)
        }
    }

    /**
     * Get movie from LiveData array
     *
     * @param [index] Movie index
     *
     * @return Movie from LiveData array by index
     */
    fun getMovie(index: Int): Movie? {
        val currentList = moviesLiveData.value
        if (currentList != null && currentList.size >= index) {
            return currentList[index]
        } else {
            return null
        }
    }

    /**
     * Get movie from LiveData array
     *
     * @return Movie array from LiveData
     */
    fun getMovieArrayList(): LiveData<ArrayList<Movie>> {
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