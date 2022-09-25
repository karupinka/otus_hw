package ru.yandex.repinanr.movies.app

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.yandex.repinanr.movies.BuildConfig
import ru.yandex.repinanr.movies.data.Const.BASE_URL
import ru.yandex.repinanr.movies.data.network.service.MoviesService

class App : Application() {
    lateinit var movieService: MoviesService

    override fun onCreate() {
        super.onCreate()

        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        movieService = retrofit.create(MoviesService::class.java)
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
