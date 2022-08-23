package ru.yandex.repinanr.movies.moviesList

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.DataModel
import ru.yandex.repinanr.movies.data.DataSource
import ru.yandex.repinanr.movies.databinding.MoviesRecycleBinding
import ru.yandex.repinanr.movies.moviesDetails.MoviesDetailFragment

class MoviesListFragment: Fragment() {
    private var adapter: MovieAdapter? = null
    private lateinit var binding: MoviesRecycleBinding
    private val dataSource = DataSource.getDataSource()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MoviesRecycleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        dataSource.getMovieArrayList().observe(viewLifecycleOwner) {
            adapter?.submitList(it.toList())
        }
        activity.let {
            val bottomNav = it?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav?.let {
                it.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Init Adapter and RecyclerView
     */
    private fun initAdapter() {
        binding.apply {
            adapter = MovieAdapter()
            adapter?.let {
                with(it) {
                    setListener(object : MovieAdapter.MovieListener {
                        override fun onItemClickListener(movie: DataModel.Movie, position: Int) {
                            parentFragmentManager.beginTransaction()
                                .add(
                                    R.id.fragment,
                                    MoviesDetailFragment.newInstance(movie),
                                    "Detail"
                                )
                                .addToBackStack("Detail")
                                .commit()
                        }

                        override fun onFavoriteClickListener(
                            movie: DataModel.Movie,
                            position: Int
                        ) {
                            val updateMovie = DataModel.Movie(
                                movieId = movie.movieId,
                                name = movie.name,
                                description = movie.description,
                                image = movie.image,
                                isFavorite = !movie.isFavorite,
                                comment = movie.comment
                            )
                            dataSource.changeMovie(movie, updateMovie)
                        }
                    })

                    rcMovie.adapter = adapter

                    val spanCount =
                        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
                    rcMovie.layoutManager =
                        GridLayoutManager(this@MoviesListFragment.context, spanCount)

                    rcMovie.addItemDecoration(RecyclerViewItemDecoration())
                    rcMovie.itemAnimator = MovieItemAnimator()
                }
            }
        }
    }
}