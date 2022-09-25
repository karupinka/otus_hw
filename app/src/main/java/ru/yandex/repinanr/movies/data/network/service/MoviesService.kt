package ru.yandex.repinanr.movies.data.network.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.yandex.repinanr.movies.data.Const.API_KEY
import ru.yandex.repinanr.movies.data.model.ItemResponse
import ru.yandex.repinanr.movies.data.model.MovieResponse

interface MoviesService {

    @Headers("x-api-key: $API_KEY")
    @GET("/api/v2.2/films/{id}")
    suspend fun getMovie(@Path("id") id: Int): Response<MovieResponse>

    @Headers("x-api-key: $API_KEY")
    @GET("/api/v2.2/films?order=RATING&type=ALL&ratingFrom=0&ratingTo=10&yearFrom=1000&yearTo=3000")
    suspend fun getMovies(@Query("page") page: Int): Response<ItemResponse>
}