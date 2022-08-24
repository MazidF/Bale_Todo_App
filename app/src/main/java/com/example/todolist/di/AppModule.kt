package com.example.todolist.di

import com.example.todolist.data.IDataSource
import com.example.todolist.data.repository.Repository
import com.example.todolist.di.qualifier.DispatcherIO
import com.example.todolist.di.qualifier.Local
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        @Local local: IDataSource,
        @DispatcherIO dispatcher: CoroutineDispatcher,
    ): Repository {
        return Repository(
            local = local,
            dispatcher = dispatcher,
        )
    }
}