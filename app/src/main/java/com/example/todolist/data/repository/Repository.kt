package com.example.todolist.data.repository

import com.example.todolist.data.IDataSource
import com.example.todolist.data.model.entity.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class Repository(
    private val local: IDataSource,
    private val dispatcher: CoroutineDispatcher,
) {

    suspend fun insert(vararg items: Task): List<Long> {
        return local.insert(*items)
    }

    suspend fun update(vararg items: Task): Int {
        return local.update(*items)
    }

    suspend fun delete(vararg items: Task): Int {
        return local.delete(*items)
    }

    suspend fun getItem(id: Long): Task? {
        return local.getItem(id)
    }

    fun getAll(): Flow<List<Task>> {
        return local.getAll()
    }

    suspend fun switch(from: Task, to: Task) {
        return local.switchTasks(from, to)
    }

    suspend fun deleteCompleted() {
        return local.clearCompleted()
    }
}