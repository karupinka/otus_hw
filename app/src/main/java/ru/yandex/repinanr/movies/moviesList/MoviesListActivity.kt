package ru.yandex.repinanr.movies.moviesList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import ru.yandex.repinanr.movies.app.App.Companion.getIndex
import ru.yandex.repinanr.movies.data.Const.CHOOSE_INDEX
import ru.yandex.repinanr.movies.data.Const.MOVIE_COMMENT
import ru.yandex.repinanr.movies.data.Const.MOVIE_FAVORITE
import ru.yandex.repinanr.movies.data.Const.MOVIE_ID
import ru.yandex.repinanr.movies.data.Const.MOVIE_KEY
import ru.yandex.repinanr.movies.data.DataSource
import ru.yandex.repinanr.movies.databinding.ActivityMainBinding
import ru.yandex.repinanr.movies.moviesDetails.MovieDetailsActivity

class MoviesListActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    lateinit var adapter: MovieAdapter
    val dataSource = DataSource.getDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        initAdapter()

        dataSource.getMovieArrayList().observe(this) {
            adapter.notifyDataSetChanged()
        }

        savedInstanceState?.let {
            val value = handleState(it)
            adapter.updateChooseMovieId(value)
            adapter.notifyDataSetChanged()
        }
    }

    /* Init RecyclerView */
    private fun initAdapter() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val bundle = result.data?.extras

                    if (bundle != null) {
                        val comment = bundle.getString(MOVIE_COMMENT) ?: ""
                        val isFavorite = bundle.getBoolean(MOVIE_FAVORITE)
                        val id = bundle.getInt(MOVIE_ID)

                        dataSource.changeMovie(getIndex(id), comment, isFavorite)

                        Log.i("Saved comment and isFavorite", "isFavorite: $isFavorite; comment: $comment")
                    }
                }
                adapter.notifyDataSetChanged()
            }

        mainBinding.apply {
            rcMovie.layoutManager = GridLayoutManager(this@MoviesListActivity, 2)
            adapter =
                MovieAdapter { movie -> adapterOnClick(movie) }

            rcMovie.adapter = adapter
            rcMovie.layoutManager
        }
    }

    /**
     * Opens MovieDetailActivity when RecyclerView item is clicked
     *
     * @param [movie_id] Movie id
     */
    private fun adapterOnClick(movie_id: Int) {
        val intent = Intent(this, MovieDetailsActivity()::class.java)

        intent.putExtra(MOVIE_KEY, movie_id)
        resultLauncher?.launch(intent)
    }

    /**
     * Save CHOOSE_INDEX movie state
     *
     * @param [outState] Bundle for save state
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val value = adapter.getChooseMovieId()
        outState.putInt(CHOOSE_INDEX, adapter.getChooseMovieId())
    }

    /**
     * Restore InstanceState
     *
     * @param [savedInstanceState] Saved Instance
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("savedInstanceState", "${handleState(savedInstanceState)}")
    }

    /**
     * Get CHOOSE_INDEX movie state
     *
     * @param [savedInstanceState] Saved Instance
     */
    private fun handleState(savedInstanceState: Bundle): Int {
        return savedInstanceState.getInt(CHOOSE_INDEX, -1)
    }
}