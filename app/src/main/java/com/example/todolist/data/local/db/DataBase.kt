package com.example.todolist.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.data.local.db.dao.TaskDao
import com.example.todolist.data.model.entity.Task

@Database(
    entities = [
        Task::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class DataBase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}