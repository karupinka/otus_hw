package ru.yandex.repinanr.movies.presentation.favoriteMovies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.model.DataModel.Movie
import ru.yandex.repinanr.movies.presentation.common.MovieItemDiffUtilCallback
import ru.yandex.repinanr.movies.presentation.common.MovieListener
import ru.yandex.repinanr.movies.presentation.common.MovieViewHolder

class FavoriteMovieAdapter:
    ListAdapter<Movie, MovieViewHolder>(MovieItemDiffUtilCallback()) {
    private lateinit var listener:  MovieListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layout = when (viewType) {
            TYPE_MOVIE -> R.layout.movie_item
            else -> throw ClassNotFoundException("Error recycler view")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position, listener, true)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is Movie -> TYPE_MOVIE
            else -> throw ClassNotFoundException("Error recycler view")
        }
    }

    fun setListener(listener: MovieListener) {
        this.listener = listener
    }

    companion object {
        val TYPE_MOVIE = 0
    }
}
