package com.renad.tabea.core.di

import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.domain.usecase.AddTaskUseCase
import com.renad.tabea.domain.usecase.CompleteTaskUseCase
import com.renad.tabea.domain.usecase.DeleteTaskUseCase
import com.renad.tabea.domain.usecase.DeleteTasksUseCase
import com.renad.tabea.domain.usecase.GetTasksUseCase
import com.renad.tabea.domain.usecase.InBoxScreenUseCases
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
    fun provideTaskUseCase(
        getTasksUseCase: GetTasksUseCase,
        deleteTaskUseCase: DeleteTaskUseCase,
        addTaskUseCase: AddTaskUseCase,
        completeTaskUseCase: CompleteTaskUseCase,
        deleteTasksUseCase: DeleteTasksUseCase,
    ): InBoxScreenUseCases = InBoxScreenUseCases(
        getTasksUseCase,
        deleteTaskUseCase,
        addTaskUseCase,
        completeTaskUseCase,
        deleteTasksUseCase,
    )
}
