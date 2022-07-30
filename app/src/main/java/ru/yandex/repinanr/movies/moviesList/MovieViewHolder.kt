package ru.yandex.repinanr.movies.moviesList

import android.view.View
import android.view.View.VISIBLE
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.DataModel
import ru.yandex.repinanr.movies.data.DataModel.Movie
import ru.yandex.repinanr.movies.databinding.MovieItemBinding

class MovieViewHolder(item: View): RecyclerView.ViewHolder(item) {
    private lateinit var movieItemBinding: MovieItemBinding

    private fun bindMovie(item: Movie, position: Int, listener: MovieAdapter.MovieListener, isFavoriteActivity: Boolean) {
        movieItemBinding = MovieItemBinding.bind(itemView)

        with(movieItemBinding) {
            val chooseColor = if (chooseItemPosition == position && !isFavoriteActivity) R.color.purple_500 else R.color.black
            val chooseColorList = ContextCompat.getColorStateList(itemView.context, chooseColor)
            ivMovieItemImg.setImageResource(item.image)
            tvMovieItmName.text = item.name
            tvRcDesription.text = item.description
            val favoriteColor = if (item.isFavorite) R.color.red else R.color.gray
            val csl = AppCompatResources.getColorStateList(itemView.context, favoriteColor)
            imageViewFavorite.imageTintList = csl
            tvMovieItmName.setTextColor(chooseColorList)

            ivMovieItemImg.setOnClickListener {
                listener.onItemClickListener(item, position)
                chooseItemPosition = position
            }
            tvMovieItmName.setOnClickListener {
                listener.onItemClickListener(item, position)
                chooseItemPosition = position
            }

            imageViewFavorite.setOnClickListener {
                listener.onFavoriteClickListener(item, position)
            }

            imageViewRemove.setOnClickListener {
                listener.onRemoveClickListener(item, position)
            }
            if (isFavoriteActivity) {
                imageViewRemove.visibility = VISIBLE
            }
        }
    }

    fun bind(dataModel: DataModel, position: Int, listener: MovieAdapter.MovieListener, isFavoriteActivity: Boolean = false) {
        when(dataModel) {
            is Movie -> bindMovie(dataModel, position, listener, isFavoriteActivity)
        }
    }

    companion object {
        var chooseItemPosition = -1
    }
}
