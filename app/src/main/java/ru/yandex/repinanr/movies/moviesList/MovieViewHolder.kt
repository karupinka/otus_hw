package ru.yandex.repinanr.movies.moviesList

import android.view.View
import android.view.View.VISIBLE
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.Const.NO_POSITION
import ru.yandex.repinanr.movies.data.DataModel
import ru.yandex.repinanr.movies.data.DataModel.Movie
import ru.yandex.repinanr.movies.databinding.MovieItemBinding

class MovieViewHolder(item: View): RecyclerView.ViewHolder(item) {
    private lateinit var movieItemBinding: MovieItemBinding

    private fun bindMovie(item: Movie, position: Int, listener: MovieAdapter.MovieListener, isFavoriteActivity: Boolean) {
        movieItemBinding = MovieItemBinding.bind(itemView)

        with(movieItemBinding) {
            ivMovieItemImg.setImageResource(item.image)
            tvMovieItmName.text = item.name
            tvRcDesription.text = item.description

            val chooseColor = if (chooseItemPosition == position && !isFavoriteActivity) R.color.purple_500 else R.color.black
            val chooseColorList = ContextCompat.getColorStateList(itemView.context, chooseColor)
            tvMovieItmName.setTextColor(chooseColorList)

            val favoriteColor = if (item.isFavorite) R.color.red else R.color.gray
            val csl = AppCompatResources.getColorStateList(itemView.context, favoriteColor)
            imageViewFavorite.imageTintList = csl

            ivMovieItemImg.setOnClickListener {
                listener.onItemClickListener(item, position)
                chooseItemPosition = position
                val chooseListColor = ContextCompat.getColorStateList(itemView.context, R.color.purple_500)
                tvMovieItmName.setTextColor(chooseListColor)
            }
            tvMovieItmName.setOnClickListener {
                listener.onItemClickListener(item, position)
                bindChooseItem(position)
            }

            imageViewFavorite.setOnClickListener {
                listener.onFavoriteClickListener(item, position)
                bindChooseItem(position)
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

    private fun bindChooseItem(newPosition: Int) {
        val chooseListColor = ContextCompat.getColorStateList(itemView.context, R.color.purple_500)
        movieItemBinding.tvMovieItmName.setTextColor(chooseListColor)
        chooseItemPosition = newPosition
    }

    companion object {
        var chooseItemPosition = NO_POSITION
    }
}
