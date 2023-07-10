package com.renad.tabea.core.di

import com.renad.tabea.data.TaskRepository
import com.renad.tabea.data.TaskRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: TaskRepositoryImp): TaskRepository
}
