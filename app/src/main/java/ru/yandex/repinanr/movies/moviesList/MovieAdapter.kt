package ru.yandex.repinanr.movies.moviesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.Const
import ru.yandex.repinanr.movies.data.Const.NO_POSITION
import ru.yandex.repinanr.movies.data.DataModel.Movie

class MovieAdapter(val isFavoriteActivity: Boolean = false) :
    ListAdapter<Movie, MovieViewHolder>(MovieItemDiffUtilCallback()) {
    private lateinit var listener: MovieListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layout = when (viewType) {
            TYPE_MOVIE -> R.layout.movie_item
            else -> throw ClassNotFoundException("Error recycler view")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position), position, listener, isFavoriteActivity)
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

    interface MovieListener {
        fun onFavoriteClickListener(movie: Movie, position: Int)
        fun onItemClickListener(movie: Movie, position: Int) {
            // DO NOTHING
        }

        fun onRemoveClickListener(movie: Movie, position: Int) {
            // DO NOTHING
        }
    }

    companion object {
        val TYPE_MOVIE = 0
    }
}
