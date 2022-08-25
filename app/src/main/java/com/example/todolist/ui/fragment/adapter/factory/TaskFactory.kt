package com.example.todolist.ui.fragment.adapter.factory

import android.view.View
import android.view.ViewGroup
import com.example.todolist.data.model.entity.Task
import com.example.todolist.databinding.TaskItemBinding
import com.example.todolist.ui.fragment.adapter.bindable.Bindable
import com.example.todolist.ui.fragment.adapter.bindable.BindableFactory
import com.example.todolist.utils.inflater
import com.example.todolist.utils.set
import com.example.todolist.utils.strikeThroughText

open class TaskFactory : BindableFactory<Task> {
    override fun inflate(parent: ViewGroup, viewType: Int): Bindable<Task> {
        val binding = createBinding(parent)
        return createBindable(binding)
    }

    private fun createBinding(parent: ViewGroup): TaskItemBinding {
        return TaskItemBinding.inflate(parent.inflater(), parent, false)
    }

    protected open fun createBindable(binding: TaskItemBinding): Bindable<Task> {
        return TaskItem(binding)
    }

    open class TaskItem(
        internal val binding: TaskItemBinding,
    ) : Bindable<Task> {

        override fun getRoot(): View {
            return binding.root
        }

        override fun bind(item: Task): Unit = with(binding) {
            name.apply {
                text = item.name
                strikeThroughText(item.isDone)
            }
            checkbox.set(item.isDone)
        }
    }
}