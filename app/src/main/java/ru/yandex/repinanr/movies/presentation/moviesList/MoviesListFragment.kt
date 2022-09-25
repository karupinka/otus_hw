package ru.yandex.repinanr.movies.presentation.moviesList

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.data.Const
import ru.yandex.repinanr.movies.data.Const.TAG_DETAIL_ACTIVITY_MOVIE
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.databinding.MoviesRecycleBinding
import ru.yandex.repinanr.movies.presentation.common.MovieItemAnimator
import ru.yandex.repinanr.movies.presentation.common.MovieListener
import ru.yandex.repinanr.movies.presentation.common.RecyclerViewItemDecoration
import ru.yandex.repinanr.movies.presentation.moviesDetails.MoviesDetailFragment

class MoviesListFragment : Fragment() {
    private lateinit var viewModel: MoviesListViewModel
    private var adapter: MovieAdapter? = null
    private lateinit var binding: MoviesRecycleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MoviesRecycleBinding.inflate(layoutInflater, container, false)
        parentFragmentManager.setFragmentResultListener(
            Const.TAG_DETAIL_ACTIVITY,
            this
        ) { requestKey, bundle ->
            val movie = bundle.getSerializable(TAG_DETAIL_ACTIVITY_MOVIE) as DataModel.Movie
            viewModel.changeMovie(movie)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel = ViewModelProvider(this, MoviesListViewModelFactory(App.instance))
            .get(MoviesListViewModel::class.java)
        lifecycleScope.launch {
            viewModel.fetchMoviesListLiveDataMediator()
            //viewModel.fetchMoviesListLiveData()
            viewModel.moviesList.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    adapter?.submitData(it)
                }
            }
            adapter?.loadStateFlow?.collectLatest { loadStates ->
                with(binding) {
                    progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    if (loadStates.refresh is LoadState.Error) {
                        createSnackBar(R.string.other_erorr_title).show()
                    }
                }
            }
        }
        activity.let {
            val bottomNav = it?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav?.let {
                it.visibility = View.VISIBLE
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.fetchMoviesListLiveData()
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
                    setListener(object : MovieListener {
                        override fun onItemClickListener(movie: DataModel.Movie) {
                            parentFragmentManager.beginTransaction()
                                .add(
                                    R.id.fragment,
                                    MoviesDetailFragment.newInstance(movie.movieId),
                                    "Detail"
                                )
                                .addToBackStack("Detail")
                                .commit()
                        }

                        override fun onFavoriteClickListener(
                            movie: DataModel.Movie,
                            position: Int
                        ) {
                            if (movie.isFavorite) {
                                viewModel.deleteFavoriteMovie(movie)
                            } else {
                                viewModel.addFavoriteMovie(movie)
                            }
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

    fun createSnackBar(@StringRes error: Int): Snackbar {
        val snackbar = Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.retry_error_button) {
            lifecycleScope.launch {
                viewModel.fetchMoviesListLiveData()
            }
        }
        return snackbar
    }
}