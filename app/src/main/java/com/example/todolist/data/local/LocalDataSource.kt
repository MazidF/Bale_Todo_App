package com.example.todolist.data.local

import com.example.todolist.data.IDataSource
import com.example.todolist.data.local.db.dao.TaskDao
import com.example.todolist.data.model.entity.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class LocalDataSource(
    private val taskDao: TaskDao,
    private val dispatcher: CoroutineDispatcher,
) : IDataSource {

    override suspend fun insert(vararg items: Task): List<Long> {
        return taskDao.insert(*items)
    }

    override suspend fun update(vararg items: Task): Int {
        return taskDao.update(*items)
    }

    override suspend fun delete(vararg items: Task): Int {
        return taskDao.delete(*items)
    }

    override suspend fun getItem(id: Long): Task? {
        return taskDao.getItem(id)
    }

    override fun getAll(): Flow<List<Task>> {
        return taskDao.getAll().flowOn(dispatcher)
    }

    override suspend fun switchTasks(from: Task, to: Task) {
        taskDao.switch(from, to)
    }
}