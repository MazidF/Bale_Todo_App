package com.example.todolist.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.todolist.data.model.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao : IDao<Task> {

    @Query("SELECT * FROM task_table WHERE task_id = :id")
    override suspend fun getItem(id: Long): Task?

    @Query("SELECT * FROM task_table ORDER BY task_position DESC")
    override fun getAll(): Flow<List<Task>>

    @Query("DELETE FROM task_table WHERE task_is_done = 1")
    suspend fun deleteCompleted()
}