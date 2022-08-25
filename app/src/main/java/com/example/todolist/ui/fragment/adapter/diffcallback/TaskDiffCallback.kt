package com.example.todolist.ui.fragment.adapter.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.todolist.data.model.entity.Task

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}