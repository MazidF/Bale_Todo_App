package com.example.todolist.ui.fragment.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.model.entity.Task
import com.example.todolist.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMain @Inject constructor(
    private val repository: Repository,
) : ViewModel() {
    private val _tasksStateFlow = MutableStateFlow<List<Task>>(emptyList())
    val taskStateFlow = _tasksStateFlow.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getAll().collectLatest {
                _tasksStateFlow.emit(it)
            }
        }
    }

    fun check(task: Task, checked: Boolean) {
        viewModelScope.launch {
            repository.update(task.copy(isDone = checked))
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    fun insert(task: Task) {
        viewModelScope.launch {
            repository.insert(task)
        }
    }

    fun switch(from: Int, to: Int, list: List<Task>) {
        viewModelScope.launch {
            val size = list.size
            val newList = when {
                from < to -> { // up to down
                    List(to - from + 1) {
                        list[(it + 1) % size].copy(position = list[it].position)
                    }
                }
                to < from -> { // down to up
                    List(from - to + 1) {
                        list[it].copy(position = list[(it + 1) % size].position)
                    }
                }
                else -> {
                    return@launch
                }
            }
            repository.update(*newList.toTypedArray())
        }
    }

    fun clearCompleted() {
        viewModelScope.launch {
            repository.deleteCompleted()
        }
    }

    private var filter = Filter.ALL

    fun getFilter(): Filter {
        return filter
    }

    fun changeFilter(filter: Filter) {
        this.filter = filter
    }

    enum class Filter(
        val action: List<Task>.() -> List<Task>,
    ) {
        ALL({
            this
        }),
        ACTIVE({
            this.filter {
                it.isDone.not()
            }
        }),
        COMPLETED({
            this.filter {
                it.isDone
            }
        });
    }
}
