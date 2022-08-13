package ru.yandex.repinanr.movies.favoriteMovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.DataModel
import ru.yandex.repinanr.movies.data.DataSource
import ru.yandex.repinanr.movies.databinding.ActivityFavoriteMovieBinding
import ru.yandex.repinanr.movies.moviesList.MovieAdapter
import ru.yandex.repinanr.movies.moviesList.MovieItemAnimator

class FavoriteMovieFragment: Fragment() {
    private var adapter: MovieAdapter? = null
    private lateinit var binding: ActivityFavoriteMovieBinding
    val dataSource = DataSource.getDataSource()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityFavoriteMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        dataSource.getMovieArrayList().observe(viewLifecycleOwner) {
            adapter?.submitList(it.filter { it.isFavorite })
        }
        activity.let {
            val bottomNav = it?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav?.let { it.visibility = View.VISIBLE }
        }
    }

    /**
     * Init Adapter and RecyclerView
     */
    private fun initAdapter() {
        binding.apply {
            adapter = MovieAdapter(isFavoriteActivity = true)

            adapter?.let {
                it.setListener(object : MovieAdapter.MovieListener {
                override fun onFavoriteClickListener(movie: DataModel.Movie, position: Int) {
                    removeMovie(it, movie, position)
                }

                override fun onRemoveClickListener(movie: DataModel.Movie, position: Int) {
                    removeMovie(it, movie, position)
                }
            })}

            rcFavorite.adapter = adapter

            rcFavorite.layoutManager = LinearLayoutManager(this@FavoriteMovieFragment.context)
            rcFavorite.itemAnimator = MovieItemAnimator()
            val dividerItemDecoration =
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            context?.let {
                ContextCompat.getDrawable(it, R.drawable.divider_drawable)
                    ?.let { dividerItemDecoration.setDrawable(it) }
            }
            rcFavorite.addItemDecoration(dividerItemDecoration)
        }
    }

    private fun removeMovie(adapter: MovieAdapter, movie: DataModel.Movie, position: Int) {
        val updateMovie = DataModel.Movie(
            movieId = movie.movieId,
            name = movie.name,
            description = movie.description,
            image = movie.image,
            isFavorite = false,
            comment = movie.comment
        )
        dataSource.changeMovie(movie, updateMovie)

        val snackbar = Snackbar.make(binding.root, R.string.toast_remove_favorite_text, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.cancel_alert_answer) {
            val cancelMovie = DataModel.Movie(
                movieId = movie.movieId,
                name = movie.name,
                description = movie.description,
                image = movie.image,
                isFavorite = true,
                comment = movie.comment
            )
            dataSource.changeMovie(updateMovie, cancelMovie)
        }
        snackbar.show()
    }
}