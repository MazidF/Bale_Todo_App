package com.example.todolist.ui.fragment.itemtouchhelper

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchCallback {
    fun onSwiped(position: Int, direction: Int)
    fun onMove(fromPosition: Int, toPosition: Int): Boolean
    fun onDrag(viewHolder: RecyclerView.ViewHolder)
    fun onClear()
}