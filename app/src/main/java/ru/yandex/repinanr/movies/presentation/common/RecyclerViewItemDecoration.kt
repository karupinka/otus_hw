package ru.yandex.repinanr.movies.presentation.common

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewItemDecoration : RecyclerView.ItemDecoration() {
    private val offset = 20
    private val paint = Paint()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        paint.setColor(Color.WHITE)
        val layoutManager = parent.layoutManager

        for (child in parent.children) {
            if (layoutManager != null) {
                c.drawRoundRect(
                    layoutManager.getDecoratedLeft(child).toFloat(),
                    layoutManager.getDecoratedTop(child).toFloat(),
                    layoutManager.getDecoratedRight(child).toFloat(),
                    layoutManager.getDecoratedBottom(child).toFloat(),
                    40.0f,
                    40.0f,
                    paint
                )
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(offset, offset, offset, offset)
    }
}
