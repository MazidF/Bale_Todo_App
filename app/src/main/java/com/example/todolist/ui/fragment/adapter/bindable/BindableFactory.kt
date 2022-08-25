package com.example.todolist.ui.fragment.adapter.bindable

import android.view.ViewGroup

interface BindableFactory<T : Any> {
    fun inflate(parent: ViewGroup, viewType: Int): Bindable<T>
}