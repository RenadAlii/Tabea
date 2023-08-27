package com.renad.tabea.domain.usecase

import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.domain.TaskRepository
import com.renad.tabea.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dispatcher: Dispatcher,
) {
    operator fun invoke(task: Task): Flow<Response<Unit>> {
        return taskRepository.upsert(task).flowOn(dispatcher.io)
    }
}
