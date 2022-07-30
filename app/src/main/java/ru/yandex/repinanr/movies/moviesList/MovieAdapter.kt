package ru.yandex.repinanr.movies.moviesList

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.Const.TAG_MOVIE_ADAPTER
import ru.yandex.repinanr.movies.data.DataModel
import ru.yandex.repinanr.movies.data.DataModel.Movie

class MovieAdapter(val isFavoriteActivity: Boolean = false) :
    RecyclerView.Adapter<MovieViewHolder>() {
    private val dataArray = mutableListOf<DataModel>()
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
        holder.bind(dataArray[position], position, listener, isFavoriteActivity)
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = dataArray[position]
        return when (item) {
            is Movie -> TYPE_MOVIE
            else -> throw ClassNotFoundException("Error recycler view")
        }
    }

    fun setDataModel(movies: List<DataModel>) {
        dataArray.clear()
        dataArray.addAll(movies)
        notifyDataSetChanged()
    }

    fun updateMovie(dataModel: DataModel, position: Int) {
        dataArray[position] = dataModel
        notifyItemChanged(position)
        Log.d(TAG_MOVIE_ADAPTER, "Data $dataModel at position $position has changed")
    }

    fun removeMovie(position: Int) {
        dataArray.removeAt(position)
        notifyItemRemoved(position)
        Log.d(TAG_MOVIE_ADAPTER, "Data at position $position has removed")
    }

    fun setListener(listener: MovieListener) {
        this.listener = listener
    }

    interface MovieListener {
        fun onItemClickListener(movie: Movie, position: Int)
        fun onFavoriteClickListener(movie: Movie, position: Int)
        fun onRemoveClickListener(movie: Movie, position: Int)
    }

    companion object {
        val TYPE_MOVIE = 0
    }
}
