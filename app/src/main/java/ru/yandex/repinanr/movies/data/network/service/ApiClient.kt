package ru.yandex.repinanr.movies.data.network.service

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.yandex.repinanr.movies.BuildConfig

object ApiClient {
    private const val BASE_URL = "https://kinopoiskapiunofficial.tech"
    private const val API_KEY = ""

    val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.HEADERS
                level = HttpLoggingInterceptor.Level.BODY
            }
        })
        .addInterceptor(
            object: Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response = chain.run {
                    proceed(
                        request()
                            .newBuilder()
                            .header("X-App-Version", "1")
                            .header("X-Platform", "Android")
                            .header("x-api-key", API_KEY)
                            .build()
                    )
                }
            }
        )
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val apiService = retrofit.create(MoviesService::class.java)
}