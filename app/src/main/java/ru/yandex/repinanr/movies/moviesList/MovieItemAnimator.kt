package ru.yandex.repinanr.movies.moviesList

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.repinanr.movies.R

class MovieItemAnimator: DefaultItemAnimator() {
    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        holder?.itemView?.animation = AnimationUtils.loadAnimation(
            holder?.itemView?.context,
            R.anim.view_holder_anim_remove
        )
        return super.animateRemove(holder)
    }

    override fun getChangeDuration(): Long {
        return 500
    }
}
