package ru.yandex.repinanr.movies.data.network.service

import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.yandex.repinanr.movies.data.model.ItemResponse
import ru.yandex.repinanr.movies.data.model.MovieResponse

interface MoviesService {

    @GET("/api/v2.2/films/{id}")
    fun getMovie(@Path("id") id: Int): Flowable<MovieResponse>

    @GET("/api/v2.2/films?order=RATING&type=ALL&ratingFrom=0&ratingTo=10&yearFrom=1000&yearTo=3000")
    fun getMovies(@Query("page") page: Int): Single<ItemResponse>
}