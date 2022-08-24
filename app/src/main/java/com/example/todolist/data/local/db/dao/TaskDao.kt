package com.example.todolist.data.local.db.dao

import androidx.room.Query
import com.example.todolist.data.model.entity.Task
import kotlinx.coroutines.flow.Flow

interface TaskDao : IDao<Task> {

    @Query("SELECT * FROM task_table WHERE task_id = :id")
    override suspend fun getItem(id: Long): Task?

    @Query("SELECT * FROM task_table")
    override fun getAll(): Flow<List<Task>>

    suspend fun switch(from: Task, to: Task) {
        val newFrom = from.copy(position = -1)
        val newTo = to.copy(position = from.position)
        update(newFrom, newTo)
        update(from.copy(position = to.position))
    }
}