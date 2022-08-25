package com.example.todolist.ui.fragment.divider

import android.content.Context
import android.graphics.Canvas
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R

class ItemDivider(
    context: Context,
) : DividerItemDecoration(context, VERTICAL) {

    init {
        setupDrawable(context)
    }

    private fun setupDrawable(context: Context) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.task_item_divider) ?: return
        setDrawable(drawable)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val divider = drawable ?: return
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until (childCount - 1)) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val dividerTop: Int = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + divider.intrinsicHeight
            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            divider.draw(canvas)
        }
    }
}