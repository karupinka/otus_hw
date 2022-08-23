package ru.yandex.repinanr.movies.moviesList

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.data.Const.MOVIE_KEY
import ru.yandex.repinanr.movies.data.Const.TAG_MOVIE_LIST_ACTIVITY
import ru.yandex.repinanr.movies.data.DataModel
import ru.yandex.repinanr.movies.data.DataSource
import ru.yandex.repinanr.movies.databinding.ActivityMainBinding
import ru.yandex.repinanr.movies.favoriteMovies.FavoriteMovieActivity
import ru.yandex.repinanr.movies.moviesDetails.MovieDetailsActivity

class MoviesListActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    val dataSource = DataSource.getDataSource()
    lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        initAdapter()
        initFavoriteButton()
    }

    /**
     * Init Adapter and RecyclerView
     */
    private fun initAdapter() {
        mainBinding.apply {
            adapter = MovieAdapter()

            resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == RESULT_OK) {
                        val bundle = result.data?.extras

                        if (bundle != null) {
                            val updateMovie = bundle.getSerializable(MOVIE_KEY) as DataModel.Movie
                            val index = App.getIndex(updateMovie.movieId)
                            adapter.updateMovie(updateMovie, index)
                            Log.i(
                                TAG_MOVIE_LIST_ACTIVITY,
                                "isFavorite: ${updateMovie.isFavorite}; comment: ${updateMovie.comment}"
                            )
                        }
                    }
                }

            adapter.setListener(object : MovieAdapter.MovieListener {
                override fun onItemClickListener(movie: DataModel.Movie, position: Int) {
                    adapterOnClick(movie)
                    adapter.notifyDataSetChanged()
                }

                override fun onFavoriteClickListener(movie: DataModel.Movie, position: Int) {
                    val updateMovie = DataModel.Movie(
                        movieId = movie.movieId,
                        name = movie.name,
                        description = movie.description,
                        image = movie.image,
                        isFavorite = !movie.isFavorite,
                        comment = movie.comment
                    )
                    adapter.updateMovie(updateMovie, position)
                    dataSource.changeMovie(position, updateMovie)
                }

                override fun onRemoveClickListener(movie: DataModel.Movie, position: Int) {
                    // DO NOTHING
                }
            })

            adapter.setDataModel(getSourceArray())
            rcMovie.adapter = adapter

            val spanCount =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
            rcMovie.layoutManager = GridLayoutManager(this@MoviesListActivity, spanCount)

            rcMovie.addItemDecoration(RecyclerViewItemDecoration())
            rcMovie.itemAnimator = MovieItemAnimator()
        }
    }

    /**
     * Set FavoriteButton clickListener
     */
    fun initFavoriteButton() {
        val favoriteResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    adapter.setDataModel(getSourceArray())
                } else {
                    adapter.setDataModel(getSourceArray())
                }
            }

        mainBinding.favoriteBtn.setOnClickListener {
            val intent = Intent(this, FavoriteMovieActivity()::class.java)
            favoriteResultLauncher.launch(intent)
        }
    }

    /**
     * Opens MovieDetailActivity when RecyclerView item is clicked
     *
     * @param [movie] Movie
     */
    private fun adapterOnClick(movie: DataModel.Movie) {
        val intent = Intent(this, MovieDetailsActivity()::class.java)

        intent.putExtra(MOVIE_KEY, movie)
        resultLauncher?.launch(intent)
    }

    /**
     * Get Array with Movie data
     */
    private fun getSourceArray(): ArrayList<DataModel> {
        val array = arrayListOf<DataModel>()
        dataSource.getMovieArrayList().value?.let { array.addAll(it) }
        return array
    }
}
