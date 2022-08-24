package com.example.todolist.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = Task.TABLE_NAME,
    indices = [
        Index(
            value = ["task_position"],
            unique = true,
        ),
    ],
)
data class Task(
    @ColumnInfo(name = "task_name") val name: String,
    @ColumnInfo(name = "task_is_done") val isDone: Boolean = false,
    @ColumnInfo(name = "task_position") val position: Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id") val id: Long = 0,
) {
    companion object {
        const val TABLE_NAME = "task_table"
    }
}
