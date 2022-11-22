package ru.yandex.repinanr.movies.presentation.moviesList

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.data.Const
import ru.yandex.repinanr.movies.data.Const.TAG_DETAIL_ACTIVITY_MOVIE
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.databinding.MoviesRecycleBinding
import ru.yandex.repinanr.movies.presentation.ViewModelFactory
import ru.yandex.repinanr.movies.presentation.common.MovieItemAnimator
import ru.yandex.repinanr.movies.presentation.common.MovieListener
import ru.yandex.repinanr.movies.presentation.common.RecyclerViewItemDecoration
import ru.yandex.repinanr.movies.presentation.dialog.DateDialog
import javax.inject.Inject

class MoviesListFragment : Fragment() {
    private lateinit var viewModel: MoviesListViewModel
    private var adapter: MovieAdapter? = null
    private lateinit var binding: MoviesRecycleBinding
    private val mDisposable = CompositeDisposable()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as App).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MoviesListViewModel::class.java)
        viewModel.fetchMoviesListLiveDataMediator()
        viewModel.moviesList.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter?.submitData(it)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) binding.progressBar.visibility = VISIBLE else binding.progressBar.visibility =
                GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            createSnackBar(it).show()
        }

        adapter?.addLoadStateListener { loadState ->
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                AlertDialog.Builder(view.context)
                    .setTitle(R.string.other_erorr_title)
                    .setMessage(it.error.localizedMessage)
                    .setNegativeButton(R.string.cancel_alert_answer) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(R.string.retry_error_button) { _, _ ->
                        adapter?.retry()
                    }
                    .show()
            }
        }

        activity.let {
            val bottomNav = it?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav?.let {
                it.visibility = View.VISIBLE
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            viewModel.fetchMoviesListLiveDataMediator()
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
                            findNavController().navigate(
                                MoviesListFragmentDirections
                                    .actionMoviesListFragmentToMoviesDetailFragment(movie.movieId)
                            )
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

                        override fun onWatchLaterListener() {
                            DateDialog().show(parentFragmentManager, "DateDialog")
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
            viewModel.fetchMoviesListLiveDataMediator()
        }
        return snackbar
    }
}