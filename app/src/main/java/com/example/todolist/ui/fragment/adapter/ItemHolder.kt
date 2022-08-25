package com.example.todolist.ui.fragment.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.ui.fragment.adapter.bindable.Bindable
import com.example.todolist.ui.fragment.adapter.bindable.HolderSetup

class ItemHolder<T : Any>(
    private val bindable: Bindable<T>,
    onItemClick: OnItemClick<T>,
    setup: HolderSetup<T>?,
) : RecyclerView.ViewHolder(bindable.getRoot()) {
    var t: T? = null

    init {
        bindable.apply {
            getRoot().apply {
                setOnClickListener {
                    t?.let(onItemClick::onClick)
                }
            }
            setup?.setup(this@ItemHolder, bindable)
        }
    }

    fun bind(item: T) {
        t = item
        bindable.bind(item)
    }
}