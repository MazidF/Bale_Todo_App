package com.example.todolist.di

import android.content.Context
import androidx.room.Room
import com.example.todolist.data.IDataSource
import com.example.todolist.data.local.LocalDataSource
import com.example.todolist.data.local.db.DataBase
import com.example.todolist.data.local.db.dao.TaskDao
import com.example.todolist.di.qualifier.DispatcherIO
import com.example.todolist.di.qualifier.Local
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context,
    ): DataBase {
        return Room.databaseBuilder(
            context,
            DataBase::class.java,
            DataBase::class.java.simpleName,
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(
        dataBase: DataBase,
    ): TaskDao {
        return dataBase.taskDao()
    }

    @Provides
    @Singleton
    @Local
    fun provideLocalDataSource(
        taskDao: TaskDao,
        @DispatcherIO dispatcher: CoroutineDispatcher,
    ): IDataSource {
        return LocalDataSource(
            taskDao = taskDao,
            dispatcher = dispatcher,
        )
    }
}