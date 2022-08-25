package com.example.todolist.ui.fragment.adapter.bindable

import com.example.todolist.ui.fragment.adapter.ItemHolder

interface HolderSetup<T : Any> {
    fun setup(holder: ItemHolder<T>, bindable: Bindable<T>)
}