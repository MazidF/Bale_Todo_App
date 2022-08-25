package com.example.todolist.ui.fragment.adapter.bindable

import android.view.View

interface Bindable<T : Any> {
    fun getRoot(): View
    fun bind(item: T)
}