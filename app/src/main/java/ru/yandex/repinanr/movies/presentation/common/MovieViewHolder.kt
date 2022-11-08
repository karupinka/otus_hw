package ru.yandex.repinanr.movies.presentation.common

import android.view.View
import android.view.View.VISIBLE
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.data.Const.NO_POSITION
import ru.yandex.repinanr.movies.data.model.DataModel
import ru.yandex.repinanr.movies.data.model.DataModel.Movie
import ru.yandex.repinanr.movies.databinding.MovieItemBinding

class MovieViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private lateinit var movieItemBinding: MovieItemBinding

    private fun bindMovie(item: Movie, position: Int, listener: MovieListener, isFavoriteActivity: Boolean) {
        movieItemBinding = MovieItemBinding.bind(itemView)

        with(movieItemBinding) {
            tvMovieItmName.text = item.name
            tvRcDesription.text = item.description

            val chooseColor = if (chooseItemPosition == position && !isFavoriteActivity) R.color.purple_500 else R.color.black
            val chooseColorList = ContextCompat.getColorStateList(itemView.context, chooseColor)
            tvMovieItmName.setTextColor(chooseColorList)

            val favoriteColor = if (item.isFavorite) R.color.red else R.color.gray
            val csl = AppCompatResources.getColorStateList(itemView.context, favoriteColor)
            imageViewFavorite.imageTintList = csl

            Glide.with(ivMovieItemImg)
                .load(item.imageUrl)
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(ivMovieItemImg)

            ivMovieItemImg.setOnClickListener {
                listener.onItemClickListener(item)
                chooseItemPosition = position
                val chooseListColor = ContextCompat.getColorStateList(itemView.context, R.color.purple_500)
                tvMovieItmName.setTextColor(chooseListColor)
            }
            tvMovieItmName.setOnClickListener {
                listener.onItemClickListener(item)
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

    fun bind(dataModel: DataModel?, position: Int, listener: MovieListener, isFavoriteActivity: Boolean = false) {
        dataModel?.let {
            when(it) {
                is Movie -> bindMovie(it, position, listener, isFavoriteActivity)
            }
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
