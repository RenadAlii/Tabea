package com.renad.tabea.domain.usecase

import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.domain.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CompleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dispatcher: Dispatcher,
) {
    operator fun invoke(taskId: Int, completed: Boolean): Flow<Response<Unit>> {
        return taskRepository.updateCompleted(taskId, completed).flowOn(dispatcher.io)
    }
}
