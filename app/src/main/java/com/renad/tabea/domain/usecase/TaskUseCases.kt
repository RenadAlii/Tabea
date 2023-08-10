package com.renad.tabea.domain.usecase

data class InBoxScreenUseCases(
    val getTasksUseCase: GetTasksUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase,
    val deleteAllTasksUseCase: DeleteAllTasksUseCase,
    val addTaskUseCase: AddTaskUseCase,
    val completeTaskUseCase: CompleteTaskUseCase,
)
