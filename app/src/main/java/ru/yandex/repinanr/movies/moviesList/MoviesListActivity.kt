package ru.yandex.repinanr.movies.moviesList

import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.Const
import ru.yandex.repinanr.movies.databinding.ActivityMainBinding
import ru.yandex.repinanr.movies.dialog.SaveDataDialog
import ru.yandex.repinanr.movies.favoriteMovies.FavoriteMovieFragment
import ru.yandex.repinanr.movies.moviesDetails.MoviesDetailFragment

class MoviesListActivity : AppCompatActivity(), SaveDataDialog.SaveDataDialogListener {
    lateinit var mainBinding: ActivityMainBinding
    private var moviesListFragment: MoviesListFragment? = null
    private var favoriteMovieFragment: FavoriteMovieFragment? = null
    private lateinit var detailFragment: MoviesDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setBottomNav()

        if (savedInstanceState == null) {
            moviesListFragment = MoviesListFragment()
            setMoviesListFragmentToContainer()
        } else {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment)
            when (fragment) {
                is MoviesListFragment -> {
                    moviesListFragment = fragment
                    setMoviesListFragmentToContainer()
                }
                is FavoriteMovieFragment -> {
                    favoriteMovieFragment = fragment
                    favoriteMovieFragment?.let {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment, it)
                            .commit()
                    }
                }
                is MoviesDetailFragment -> {
                    moviesListFragment = MoviesListFragment()
                    detailFragment = fragment
                    setMoviesListFragmentToContainer()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment, detailFragment)
                        .commit()
                }
                else -> throw RuntimeException("Unknown fragment")
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            if (isDetailFragment()) {
                getDetailFragment()?.let {
                    if (it.isMovieChanged()) {
                        val dialog = SaveDataDialog()
                        dialog.show(supportFragmentManager, "SaveDataDialog")
                    } else {
                        super.onBackPressed()
                        mainBinding.bottomNavigation.visibility = VISIBLE
                    }
                }
            } else {
                supportFragmentManager.popBackStack()
                mainBinding.bottomNavigation.visibility = VISIBLE
            }
        } else {
            super.onBackPressed()
            mainBinding.bottomNavigation.visibility = VISIBLE
        }
    }

    private fun isDetailFragment(): Boolean {
        return if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name == "Detail"
        } else {
            false
        }
    }

    private fun setBottomNav() {
        mainBinding.bottomNavigation.setOnItemSelectedListener { itemMenu ->
            when (itemMenu.itemId) {
                R.id.nav_movies -> {
                    if (moviesListFragment == null) {
                        moviesListFragment = MoviesListFragment()
                    }
                    setMoviesListFragmentToContainer()
                }
                R.id.nav_favorite -> {
                    if (favoriteMovieFragment == null) {
                        favoriteMovieFragment = FavoriteMovieFragment()
                    }
                    favoriteMovieFragment?.let {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment, it)
                            .commit()
                    }
                }
            }
            true
        }
    }

    private fun getDetailFragment(): MoviesDetailFragment? {
        return supportFragmentManager.findFragmentByTag("Detail") as? MoviesDetailFragment
    }

    private fun setMoviesListFragmentToContainer() {
        moviesListFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, it)
                .addToBackStack("main")
                .commit()
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        getDetailFragment()?.let {
            it.updateMovie()
        }
        dialog.dismiss()
        Log.d(Const.TAG_DETAIL_ACTIVITY, "onDialogPositiveClick")
        supportFragmentManager.popBackStack()
        mainBinding.bottomNavigation.visibility = VISIBLE
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(this, R.string.not_save_data, Toast.LENGTH_LONG).show()
        dialog.dismiss()
        Log.d(Const.TAG_DETAIL_ACTIVITY, "onDialogNegativeClick")
        supportFragmentManager.popBackStack()
        mainBinding.bottomNavigation.visibility = VISIBLE
    }
}
