package ru.yandex.repinanr.movies.presentation.common

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.Const
import ru.yandex.repinanr.movies.databinding.ActivityMainBinding
import ru.yandex.repinanr.movies.presentation.dialog.SaveDataDialog
import ru.yandex.repinanr.movies.presentation.moviesList.MoviesListFragmentDirections

class MoviesActivity : AppCompatActivity(), SaveDataDialog.SaveDataDialogListener {
    lateinit var mainBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        navController = Navigation.findNavController(this, R.id.fragment)
        val appBarConfiguration = AppBarConfiguration
            .Builder(R.id.moviesListFragment, R.id.favoriteMovieFragment)
            .build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        setupWithNavController(mainBinding.bottomNavigation, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.moviesDetailFragment) {
                mainBinding.bottomNavigation.visibility = GONE
            } else {
                mainBinding.bottomNavigation.visibility = VISIBLE
            }
        }

        val value = intent?.extras?.get("id")?.toString() ?: ""
        if (value.isNotEmpty()) {
            findNavController(R.id.fragment).navigate(
                MoviesListFragmentDirections.actionMoviesListFragmentToMoviesDetailFragment(
                    value.toInt()
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.fragment)) || super.onOptionsItemSelected(
            item
        )
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        (dialog as SaveDataDialog).updateMovie()
        dialog.dismiss()
        Log.d(Const.TAG_DETAIL_ACTIVITY, "onDialogPositiveClick")
        findNavController(R.id.fragment).popBackStack(R.id.moviesDetailFragment, true)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(this, R.string.not_save_data, Toast.LENGTH_LONG).show()
        dialog.dismiss()
        Log.d(Const.TAG_DETAIL_ACTIVITY, "onDialogNegativeClick")
        findNavController(R.id.fragment).popBackStack(R.id.moviesDetailFragment, true)
    }
}
