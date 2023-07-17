package com.renad.tabea.core.di

import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.domain.usecase.GetTaskUseCase
import com.renad.tabea.domain.usecase.GetTasksUseCase
import com.renad.tabea.domain.usecase.TaskUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDispatcher(): Dispatcher = Dispatcher()

    @Provides
    fun provideTaskUseCase(getTasksUseCase: GetTasksUseCase, getTaskUseCase: GetTaskUseCase): TaskUseCases = TaskUseCases(getTaskUseCase, getTasksUseCase)
}
