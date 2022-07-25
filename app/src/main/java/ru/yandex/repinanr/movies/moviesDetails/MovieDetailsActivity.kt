package ru.yandex.repinanr.movies.moviesDetails

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.data.Const.MOVIE_COMMENT
import ru.yandex.repinanr.movies.data.Const.MOVIE_FAVORITE
import ru.yandex.repinanr.movies.data.Const.MOVIE_ID
import ru.yandex.repinanr.movies.data.Const.MOVIE_KEY
import ru.yandex.repinanr.movies.data.DataSource
import ru.yandex.repinanr.movies.data.Movie
import ru.yandex.repinanr.movies.databinding.ActivityMovieDetailsBinding

class MovieDetailsActivity : AppCompatActivity() {
    lateinit var movieDetailsBinding: ActivityMovieDetailsBinding
    var movie: Movie? = null
    val dataSource = DataSource.getDataSource()
    var checkBoxValueIfClicked: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieDetailsBinding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(movieDetailsBinding.root)

        val bundle: Bundle? = intent.extras

        updateMovieFromBundle(bundle)

        movie?.let {
            with(movieDetailsBinding) {
                ivMovie.setImageResource(it.image)
                tvMovieName.text = it.name
                tvMovieDescription.text = it.description
                var isFavorite = it.isFavorite
                if (isFavorite != null) {
                    checkBox.isChecked = isFavorite
                }
                etComment.setText(it.comment)
            }
        }

        movieDetailsBinding.btnShare.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.invite_message))
                type = "text/plain"
            }

            val shareIntent: Intent = Intent.createChooser(sendIntent, null)
            shareLauncher.launch(shareIntent)
        }

        movieDetailsBinding.checkBox.setOnClickListener {
            checkBoxValueIfClicked = movieDetailsBinding.checkBox.isChecked()
        }
    }

    private val shareLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val toast = Toast.makeText(
                    applicationContext,
                    getString(R.string.invite_message),
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }

    /**
     * Back Press Listener with alert dialog
     *
     * @param [bundle] Bundle from intent
     */
    fun updateMovieFromBundle(bundle: Bundle?) {
        if (bundle != null) {
            val movieId = bundle.getInt(MOVIE_KEY)
            movie = dataSource.getMovie(App.getIndex(movieId))
        }
    }

    /**
     * Back Press Listener with alert dialog
     */
    override fun onBackPressed() {
        val intent = Intent()

        movie?.let {
            if (
                (it.isFavorite != movieDetailsBinding.checkBox.isChecked &&
                        checkBoxValueIfClicked != it.isFavorite) ||
                it.comment != movieDetailsBinding.etComment.text.toString()
            ) {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.save_alert_title)
                    setMessage(R.string.save_alert_text)

                    setPositiveButton(R.string.yes_alert_answer) { _, _ ->
                        intent.putExtra(MOVIE_ID, it.movieId)

                        with(movieDetailsBinding) {
                            val movieText = etComment.text.toString()
                            intent.putExtra(MOVIE_COMMENT, movieText)
                            intent.putExtra(MOVIE_FAVORITE, checkBox.isChecked)
                        }

                        setResult(RESULT_OK, intent)
                        finish()
                    }

                    setNegativeButton(R.string.no_alert_answer) { _, _ ->
                        Toast.makeText(
                            this@MovieDetailsActivity,
                            R.string.not_save_data,
                            Toast.LENGTH_LONG
                        ).show()

                        setResult(RESULT_CANCELED)
                        finish()
                    }
                    setCancelable(true)
                }.create().show()
            } else {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
    }
}