package ru.yandex.repinanr.movies.favoriteMovies

import android.content.Intent
import android.content.res.Configuration
import android.icu.lang.UCharacter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.Const.TAG_MOVIE_FAVORITE_LIST_ACTIVITY
import ru.yandex.repinanr.movies.data.DataModel
import ru.yandex.repinanr.movies.data.DataSource
import ru.yandex.repinanr.movies.databinding.ActivityFavoriteMovieBinding
import ru.yandex.repinanr.movies.moviesList.MovieAdapter
import ru.yandex.repinanr.movies.moviesList.MovieItemAnimator
import ru.yandex.repinanr.movies.moviesList.RecyclerViewItemDecoration

class FavoriteMovieActivity : AppCompatActivity() {
    private lateinit var favoriteMovieBinding: ActivityFavoriteMovieBinding
    val dataSource = DataSource.getDataSource()
    lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteMovieBinding = ActivityFavoriteMovieBinding.inflate(layoutInflater)
        setContentView(favoriteMovieBinding.root)
        initAdapter()
    }

    /**
     * Init Adapter and RecyclerView
     */
    private fun initAdapter() {
        favoriteMovieBinding.apply {
            adapter = MovieAdapter(isFavoriteActivity = true)

            adapter.setListener(object : MovieAdapter.MovieListener {
                override fun onItemClickListener(movie: DataModel.Movie, position: Int) {
                    // DO NOTHING
                }

                override fun onFavoriteClickListener(movie: DataModel.Movie, position: Int) {
                    val updateMovie = DataModel.Movie(
                        movieId = movie.movieId,
                        name = movie.name,
                        description = movie.description,
                        image = movie.image,
                        isFavorite = false,
                        comment = movie.comment
                    )
                    dataSource.changeMovie(movie, updateMovie)
                    adapter.removeMovie(position)
                }

                override fun onRemoveClickListener(movie: DataModel.Movie, position: Int) {
                    val updateMovie = DataModel.Movie(
                        movieId = movie.movieId,
                        name = movie.name,
                        description = movie.description,
                        image = movie.image,
                        isFavorite = false,
                        comment = movie.comment
                    )
                    dataSource.changeMovie(movie, updateMovie)
                    adapter.removeMovie(position)
                }
            })

            val arrayList = arrayListOf<DataModel.Movie>()
            dataSource.getMovieArrayList().value?.let { arrayList.addAll(it) }
            adapter.setDataModel(arrayList.filter { it.isFavorite })
            rcFavorite.adapter = adapter

            rcFavorite.layoutManager = LinearLayoutManager(this@FavoriteMovieActivity)
            rcFavorite.itemAnimator = MovieItemAnimator()
            val dividerItemDecoration =
                DividerItemDecoration(this@FavoriteMovieActivity, DividerItemDecoration.VERTICAL)
            ResourcesCompat.getDrawable(resources, R.drawable.divider_drawable, theme)
                ?.let { dividerItemDecoration.setDrawable(it) }
            rcFavorite.addItemDecoration(dividerItemDecoration)
        }
    }

    /**
     * Back Press Listener with alert dialog
     */
    override fun onBackPressed() {
        var intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}
