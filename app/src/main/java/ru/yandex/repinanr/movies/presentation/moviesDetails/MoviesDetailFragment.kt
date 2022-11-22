package ru.yandex.repinanr.movies.presentation.moviesDetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.databinding.MovieDetailsBinding
import ru.yandex.repinanr.movies.presentation.ViewModelFactory
import javax.inject.Inject

class MoviesDetailFragment : Fragment() {
    private val args by navArgs<MoviesDetailFragmentArgs>()

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var binding: MovieDetailsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as App).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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
        viewModel = ViewModelProvider(this, viewModelFactory)
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
                viewModel.loadMovie(args.id)
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

        viewModel.loadMovie(args.id)

        binding.favoriteBtn.setOnClickListener {
            viewModel.changeFavoriteMovieItem()
        }

        binding.btnWatchLater.setOnClickListener {
            findNavController().navigate(
                MoviesDetailFragmentDirections
                    .actionMoviesDetailFragmentToDateDialog(args.id)
            )
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

        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                if (isMovieChanged()) {
                    val comment = (binding.etComment.text ?: "").toString()

                    findNavController().navigate(
                        MoviesDetailFragmentDirections
                            .actionMoviesDetailFragmentToSaveDataDialog(
                                comment,
                                viewModel.isFavorite,
                                viewModel.moviesItem.value as DataModel.Movie
                            )
                    )
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(
            viewLifecycleOwner,
            callback
        )
    }

    fun isMovieChanged(): Boolean {
        with(binding) {
            val comment = (etComment.text ?: "").toString()
            return viewModel.isMovieChanged(comment)
        }
    }
}