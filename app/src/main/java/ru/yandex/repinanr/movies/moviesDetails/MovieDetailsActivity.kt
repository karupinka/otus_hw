package ru.yandex.repinanr.movies.moviesDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.data.Const.MOVIE_KEY
import ru.yandex.repinanr.movies.data.Const.TAG_DETAIL_ACTIVITY
import ru.yandex.repinanr.movies.data.DataModel
import ru.yandex.repinanr.movies.data.DataSource
import ru.yandex.repinanr.movies.databinding.ActivityMovieDetailsBinding
import ru.yandex.repinanr.movies.dialog.SaveDataDialog

class MovieDetailsActivity : AppCompatActivity(), SaveDataDialog.SaveDataDialogListener {
    lateinit var movieDetailsBinding: ActivityMovieDetailsBinding
    var movie: DataModel.Movie? = null
    var checkBoxValueIfClicked: Boolean? = null
    val dataSource = DataSource.getDataSource()

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
                checkBox.isChecked = it.isFavorite
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
            movie = bundle.getSerializable(MOVIE_KEY) as? DataModel.Movie
        }
    }

    /**
     * Back Press Listener with alert dialog
     */
    override fun onBackPressed() {
        val saveDataDialog = SaveDataDialog()

        movie?.let {
            if (
                (it.isFavorite != movieDetailsBinding.checkBox.isChecked &&
                        checkBoxValueIfClicked != it.isFavorite) ||
                it.comment != movieDetailsBinding.etComment.text.toString()
            ) {
                saveDataDialog.show(supportFragmentManager, "SaveDataDialog")
            } else {
                finish()
            }
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val intent = Intent()
        with(movieDetailsBinding) {
            movie?.let {
                val updateMovie = DataModel.Movie(
                    movieId = it.movieId,
                    name = it.name,
                    description = it.description,
                    image = it.image,
                    isFavorite = checkBox.isChecked,
                    comment = etComment.text.toString()
                )
                intent.putExtra(MOVIE_KEY,updateMovie)

                val index = App.getIndex(updateMovie.movieId)
                dataSource.changeMovie(index, updateMovie)
            }
        }

        setResult(RESULT_OK, intent)
        finish()
        Log.d(TAG_DETAIL_ACTIVITY, "onDialogPositiveClick")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(
            this@MovieDetailsActivity,
            R.string.not_save_data,
            Toast.LENGTH_LONG
        ).show()

        setResult(RESULT_CANCELED)
        finish()
        Log.d(TAG_DETAIL_ACTIVITY, "onDialogNegativeClick")
    }
}
