package ru.yandex.repinanr.movies.moviesList

import androidx.recyclerview.widget.DiffUtil
import ru.yandex.repinanr.movies.data.DataModel

class MovieItemDiffUtilCallback: DiffUtil.ItemCallback<DataModel.Movie>() {
    override fun areItemsTheSame(oldItem: DataModel.Movie, newItem: DataModel.Movie): Boolean {
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItem: DataModel.Movie, newItem: DataModel.Movie): Boolean {
        return oldItem == newItem
    }
}