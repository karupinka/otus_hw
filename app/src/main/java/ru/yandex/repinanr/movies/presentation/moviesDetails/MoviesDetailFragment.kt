package ru.yandex.repinanr.movies.presentation.moviesDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.data.Const.MOVIE_KEY
import ru.yandex.repinanr.movies.data.Const.TAG_DETAIL_ACTIVITY
import ru.yandex.repinanr.movies.data.Const.TAG_DETAIL_ACTIVITY_MOVIE
import ru.yandex.repinanr.movies.databinding.MovieDetailsBinding

class MoviesDetailFragment : Fragment() {
    private lateinit var viewModel: MovieDetailViewModel
    private var movieId: Int = 0
    private lateinit var binding: MovieDetailsBinding

    private val shareLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val toast = Toast.makeText(
                    this.context,
                    getString(R.string.invite_message),
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = requireArguments().getInt(MOVIE_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MovieDetailsBinding.inflate(layoutInflater, container, false)
        activity.let {
            val bottomNav = it?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav?.let { it.visibility = View.INVISIBLE }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, MovieDetailsViewModelFactory(App.instance))
            .get(MovieDetailViewModel::class.java)
        viewModel.moviesItem.observe(viewLifecycleOwner) {
            with(binding) {
                Glide.with(ivMovie)
                    .load(it.imageUrl)
                    .error(R.drawable.ic_error)
                    .centerCrop()
                    .into(ivMovie)
                tvMovieName.text = it.name
                tvMovieDescription.text = it.description
                val favoriteColor = if (it.isFavorite) R.color.red else R.color.white
                val csl = AppCompatResources.getColorStateList(view.context, favoriteColor)
                binding.favoriteBtn.imageTintList = csl
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
            snackbar.setAction(R.string.retry_error_button) {
                viewModel.getMovie(movieId)
            }
            snackbar.show()
        }
        viewModel.commentMessage.observe(viewLifecycleOwner) {
            binding.etComment.setText(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBarDetail.visibility = View.VISIBLE
            } else {
                binding.progressBarDetail.visibility = View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.getFavoriteMovie(movieId)
            viewModel.getMovie(movieId)
            viewModel.getMovieComment(movieId)
        }

        binding.favoriteBtn.setOnClickListener {
            viewModel.changeFavoriteMovieItem()
        }

        binding.btnShare.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.invite_message))
                type = "text/plain"
            }

            val shareIntent: Intent = Intent.createChooser(sendIntent, null)
            shareLauncher.launch(shareIntent)
        }
    }

    fun updateMovie() {
        with(binding) {
            val comment = (etComment.text ?: "").toString()
            if (viewModel.isMovieChanged(comment)) {
                viewModel.saveMovieComment(movieId, comment)
                viewModel.addDeleteFavoriteMovieDB()
                val bundle = Bundle()
                bundle.putSerializable(TAG_DETAIL_ACTIVITY_MOVIE, viewModel.moviesItem.value)
                parentFragmentManager.setFragmentResult(TAG_DETAIL_ACTIVITY, bundle)
            }
        }
    }

    fun isMovieChanged(): Boolean {
        with(binding) {
            val comment = (etComment.text ?: "").toString()
            return viewModel.isMovieChanged(comment)
        }
    }

    companion object {
        fun newInstance(movieId: Int): MoviesDetailFragment {
            val args = Bundle()
            args.putSerializable(MOVIE_KEY, movieId)
            val fragment = MoviesDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}