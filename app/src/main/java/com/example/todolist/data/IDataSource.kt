package com.example.todolist.data

import com.example.todolist.data.model.entity.Task
import kotlinx.coroutines.flow.Flow

interface IDataSource {

    suspend fun insert(vararg items: Task): List<Long>

    suspend fun update(vararg items: Task): Int

    suspend fun delete(vararg items: Task): Int

    suspend fun getItem(id: Long): Task?

    fun getAll(): Flow<List<Task>>

    suspend fun switchTasks(from: Task, to: Task)
}
