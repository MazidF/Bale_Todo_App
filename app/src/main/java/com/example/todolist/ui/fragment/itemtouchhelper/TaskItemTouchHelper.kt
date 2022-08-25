package com.example.todolist.ui.fragment.itemtouchhelper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView


class TaskItemTouchHelper(
    private val callback: ItemTouchCallback,
) : ItemTouchHelper.SimpleCallback(
    UP or DOWN, RIGHT or LEFT,
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.position()
        val to = target.position()
        return callback.onMove(from, to)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.position()
        callback.onSwiped(position, direction)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        when (actionState) {
            ACTION_STATE_DRAG -> {
                if (viewHolder != null) {
                    callback.onDrag(viewHolder)
                }
            }
            ACTION_STATE_IDLE -> {
                if (viewHolder == null) {
                    callback.onClear()
                }
            }
        }
    }

    private fun RecyclerView.ViewHolder.position(): Int {
        return layoutPosition // because of 'ConcatAdapter' we use layoutPosition
    }

    companion object {
        fun attachToRecyclerView(recyclerView: RecyclerView, itemTouchCallback: ItemTouchCallback) {
            ItemTouchHelper(
                TaskItemTouchHelper(itemTouchCallback)
            ).attachToRecyclerView(recyclerView)
        }
    }
}
