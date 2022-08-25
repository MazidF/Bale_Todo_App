package com.example.todolist.ui.fragment.adapter

interface OnItemClick<T : Any> {
    fun onClick(item: T)

    companion object {
        fun <T : Any> fake(): OnItemClick<T> {
            return object : OnItemClick<T> {
                override fun onClick(item: T) {}
            }
        }
    }
}