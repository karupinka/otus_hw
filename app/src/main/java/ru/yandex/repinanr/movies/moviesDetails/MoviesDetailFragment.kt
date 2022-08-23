package ru.yandex.repinanr.movies.moviesDetails

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.Const.MOVIE_KEY
import ru.yandex.repinanr.movies.data.DataModel.Movie
import ru.yandex.repinanr.movies.data.DataSource
import ru.yandex.repinanr.movies.databinding.MovieDetailsBinding

class MoviesDetailFragment : Fragment() {
    private var movie: Movie? = null
    private var changeMovie: Movie? = null
    private lateinit var binding: MovieDetailsBinding
    private val dataSource = DataSource.getDataSource()

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
        movie = requireArguments().getSerializable(MOVIE_KEY) as? Movie
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MovieDetailsBinding.inflate(layoutInflater, container, false)
        activity.let {
            val bottomNav = it?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav?.let { it.visibility = View.INVISIBLE }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie?.let {
            with(binding) {
                ivMovie.setImageResource(it.image)
                tvMovieName.text = it.name
                tvMovieDescription.text = it.description
                etComment.setText(it.comment)
                val favoriteColor = if (it.isFavorite) R.color.red else R.color.white
                val csl = AppCompatResources.getColorStateList(view.context, favoriteColor)
                binding.favoriteBtn.imageTintList = csl
            }
        }

        binding.favoriteBtn.setOnClickListener {
            val movieItem = changeMovie ?: movie
            movieItem?.let {
                changeMovie = it.copy(isFavorite = !it.isFavorite)
                val favoriteColor =
                    if (changeMovie?.isFavorite ?: false) R.color.red else R.color.gray
                val csl = AppCompatResources.getColorStateList(view.context, favoriteColor)
                binding.favoriteBtn.imageTintList = csl
            }
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

    fun isMovieChanged(): Boolean {
        val comment = binding.etComment.text.toString()
        changeMovie?.let {
            changeMovie = it.copy(comment = comment)
            return changeMovie != movie
        }
        movie?.let {
            if (it.comment != comment) {
                changeMovie = it.copy(comment = comment)
                return true
            } else {
                return false
            }
        }
        return false
    }

    fun updateMovie() {
        with(binding) {
            movie?.let { movie ->
                if (isMovieChanged()) {
                    changeMovie?.let { changeMovie ->
                        with(dataSource) {
                            changeMovie(movie, changeMovie)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val VALUE_ARG = "value"

        fun newInstance(movie: Movie): MoviesDetailFragment {
            val args = Bundle()
            args.putSerializable(MOVIE_KEY, movie)
            val fragment = MoviesDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}