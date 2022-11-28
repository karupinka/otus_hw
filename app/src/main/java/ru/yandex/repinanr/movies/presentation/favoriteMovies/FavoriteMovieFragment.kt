package ru.yandex.repinanr.movies.presentation.favoriteMovies

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.databinding.ActivityFavoriteMovieBinding
import ru.yandex.repinanr.movies.presentation.ViewModelFactory
import ru.yandex.repinanr.movies.presentation.common.MovieItemAnimator
import ru.yandex.repinanr.movies.presentation.common.MovieListener
import ru.yandex.repinanr.movies.presentation.dialog.DateDialog
import javax.inject.Inject

class FavoriteMovieFragment : Fragment() {
    private var adapter: FavoriteMovieAdapter? = null
    private lateinit var viewModel: FavoriteMovieViewModel
    private lateinit var binding: ActivityFavoriteMovieBinding

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
        binding = ActivityFavoriteMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (binding.rcFavorite.visibility != View.GONE) {
            initAdapter()
            viewModel = ViewModelProvider(this, viewModelFactory)
                .get(FavoriteMovieViewModel::class.java)
            viewModel.moviesList.observe(viewLifecycleOwner) {
                adapter?.submitList(it)
            }
            activity.let {
                val bottomNav = it?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNav?.let { it.visibility = View.VISIBLE }
            }
            viewModel.errorMessage.observe(viewLifecycleOwner) {
                val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.retry_error_button) {
                    viewModel.getFavoriteMovies()
                }
                snackbar.show()
            }
            viewModel.getFavoriteMovies()
        }
    }

    /**
     * Init Adapter and RecyclerView
     */
    private fun initAdapter() {
        binding.apply {
            adapter = FavoriteMovieAdapter()

            adapter?.let {
                it.setListener(object : MovieListener {
                    override fun onFavoriteClickListener(movie: DataModel.Movie, position: Int) {
                        removeMovie(movie, position)
                    }

                    override fun onRemoveClickListener(movie: DataModel.Movie, position: Int) {
                        removeMovie(movie, position)
                    }

                    override fun onWatchLaterListener() {
                        DateDialog().show(parentFragmentManager, "DateDialog")
                    }
                })
            }

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

    private fun removeMovie(movie: DataModel.Movie, position: Int) {
        viewModel.removeFavoriteMovie(movie)
        val snackbar =
            Snackbar.make(binding.root, R.string.toast_remove_favorite_text, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.cancel_alert_answer) {
            viewModel.cancelFavoriteMoviesRemove(position)
        }
        snackbar.show()
    }
}