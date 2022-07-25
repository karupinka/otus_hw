package ru.yandex.repinanr.movies.moviesList

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.DataSource
import ru.yandex.repinanr.movies.data.Movie
import ru.yandex.repinanr.movies.databinding.MovieItemBinding


class MovieAdapter(private val onClick: (Int) -> Unit): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    val dataSource = DataSource.getDataSource()
    private lateinit var holder: MovieViewHolder

    class MovieViewHolder(item: View, val onClick: (Int) -> Unit): RecyclerView.ViewHolder(item) {
        val movieItemBinding = MovieItemBinding.bind(item)
        private var currentMovie: Movie? = null

        init {
            item.setOnClickListener {
                currentMovie?.let {
                    chooseMovieId = it.movieId
                    onClick(chooseMovieId)
                }
            }
        }

        fun bind(item: Movie?, position: Int) {
            item?.let {
                with(movieItemBinding) {
                    currentMovie = item
                    ivMovieItemImg.setImageResource(item.image)
                    tvMovieItmName.text = item.name
                    tvRcDesription.text = item.description
                    val isFavorite = item.isFavorite
                    if (isFavorite != null) {
                        with(checkBoxMovieItem) {
                            checkBoxMovieItem.visibility = VISIBLE
                            checkBoxMovieItem.isChecked = isFavorite
                            checkBoxMovieItem.setOnClickListener {
                                item.isFavorite = checkBoxMovieItem.isChecked
                            }
                        }
                    }

                    val color = if (chooseMovieId == it.movieId) Color.BLUE else Color.BLACK
                    movieItemBinding.tvMovieItmName.setTextColor(color)
                }
            }
        }

        companion object {
            var chooseMovieId = -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        holder = MovieViewHolder(view, onClick)
        return holder
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val arrayList = dataSource.getMovieArrayList().value
        holder.bind(arrayList?.get(position), position)
    }

    override fun getItemCount(): Int {
        val arrayList = dataSource.getMovieArrayList().value
        return arrayList?.size ?: 0
    }

    /**
     * Update choose item
     *
     * @param [movieId] Movie id
     */
    fun updateChooseMovieId(movieId: Int) {
        MovieViewHolder.chooseMovieId = movieId
        notifyDataSetChanged()
    }

    /**
     * Update choose item
     *
     * @return Choose movie id
     */
    fun getChooseMovieId() = MovieViewHolder.chooseMovieId
}