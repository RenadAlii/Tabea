package com.renad.tabea.domain.usecase

data class InBoxScreenUseCases(
    val getTasksUseCase: GetTasksUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase,
    val addTaskUseCase: AddTaskUseCase,
    val completeTaskUseCase: CompleteTaskUseCase,
    val deleteTasksUseCase: DeleteTasksUseCase
)
