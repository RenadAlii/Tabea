package com.renad.tabea.domain.usecase

import com.renad.tabea.R
import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.domain.TaskRepository
import com.renad.tabea.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dispatcher: Dispatcher,
) {
    /**
     * Returns a list of tasks.
     *
     * @param taskId - the field used to sort the tasks. Default NONE = ASC sort.
     */
    operator fun invoke(taskId: Int): Flow<Response<Task>> {
        return taskRepository.getTaskById(taskId).transform { response ->
            when (response) {
                is Response.Success -> {
                    if (response.data == null) {
                        emit(Response.Failure(R.string.task_not_found))
                    }
                }

                else -> {}
            }
        }.flowOn(dispatcher.io)
    }
}
